package factory1;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import common.DoWrap;

import java.lang.reflect.*;
import java.util.Map;


/**
 * Uses a static "wrap" method to initialize wrapping process for a class, creating and assigning @FunctionWrapper annotated static Function
 * fields, by matching and wrapping corresponding @FunctionWrapper annotated no-arg, non-void methods (i.e., typically POJO getters)
 */
public class FunctionFactory {

    public static <F, T> void wrap(Class<F> clazz) {
        final Map<String, Method> wraps = findWrappedMethods(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(FunctionWrapper.class)) {
                final Method method = validateMapping(wraps, field);
                Function<F, T> wrapper = DoWrap.createFunctionToWrap(method);
                try {
                    field.set(null, wrapper);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static <F> Map<String, Method> findWrappedMethods(Class<F> clazz) {
        final Map<String, Method> wraps = Maps.newHashMap();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(FunctionWrapped.class)) {
                FunctionWrapped a = validateMethod(method, wraps);
                wraps.put(a.value(), method);
            }
        }
        return wraps;
    }

    private static <F> Map<String, Field> findWrappedFields(Class<F> clazz) {
        final Map<String, Field> wraps = Maps.newHashMap();
        for (Field field: clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(FunctionWrapper.class)) {
                FunctionWrapper a = validateField(field);
                wraps.put(a.value(), field);
            }
        }
        return wraps;
    }

    private static FunctionWrapped validateMethod(Method method, Map<String, Method> namedMethods) {
        checkArgument(method.getParameterTypes().length == 0,
                "Method %s has arguments and may not be wrapped as a function", method.getName() );
        FunctionWrapped fwAnnot = method.getAnnotation(FunctionWrapped.class);

        // every @FunctionWrapped annotated method in a class must have a unique value
        Preconditions.checkArgument(!namedMethods.containsKey(fwAnnot.value()),
                "Duplicate values (\"%s\") in @FunctionWrapped annotated methods in this class", fwAnnot.value());
        return fwAnnot;
    }

    private static FunctionWrapper validateField(Field field) {
        checkArgument(Modifier.isStatic(field.getModifiers()),
                "@%s may not be applied to field %s because it is not static", FunctionWrapper.class.getName(), field.getName());
        Class fieldClass = field.getType();
        checkArgument(fieldClass.isAssignableFrom(Function.class),
                "@%s annotated field %s is not assignable from Function", FunctionWrapper.class.getName(), field.getName());

        // TODO depending on how this ends up, I may want to check that it is final too

        TypeVariable[] parmTypes = fieldClass.getTypeParameters();
        // TODO check against <F, T> if I can figure how to do that

        return field.getAnnotation(FunctionWrapper.class);
    }

    private static Method validateMapping(Map<String, Method> wraps, Field field) {
        FunctionWrapper a = validateField(field);
        final String value = a.value();
        final Method method = wraps.get(value);
        checkArgument(method != null, "@%s Function %s has no matching @%s with same value %s",
                FunctionWrapper.class.getName(), field.getName(), FunctionWrapped.class.getName(), value);
        return method;
    }
}

