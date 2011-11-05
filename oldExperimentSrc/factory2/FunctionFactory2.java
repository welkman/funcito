package factory2;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import common.DoWrap;

public class FunctionFactory2 {

    private static final Map<Class, Map<String, Method>> annotatedMethods = Maps.newHashMap();
    private static final Map<Class, Map<String, Field>> unprocessedFunctionFields = Maps.newHashMap();
    private static final Set<Class> processedClasses = Sets.newHashSet();

    public static final <F, T> Function<F, T> wrap(Class<F> clazz, String value) {
        Method matchedMethod = null;
        synchronized (clazz) {
            if (!processedClasses.contains(clazz)) {
                processedClasses.add(clazz);
                Map<String, Method> annotatedMethodsInClass = findWrappedMethods(clazz);
                Map<String, Field>  annotatedFieldsInClass  = findWrappedFields(clazz);
                // ensure that every annotated field has at least one matching annotated method
                Set diff = Sets.difference(annotatedFieldsInClass.keySet(), annotatedMethodsInClass.keySet());
                checkState(diff.isEmpty(),
                        "Some Functions with annotation values (%s) have no corresponding annotated methods with matching value.",
                        Joiner.on(", ").join(diff));
                annotatedFieldsInClass.remove(value); // cleanup to reduce residual footprint
                annotatedMethods.put(clazz, annotatedMethodsInClass);
                unprocessedFunctionFields.put(clazz, annotatedFieldsInClass);
            }
            matchedMethod = annotatedMethods.get(clazz).get(value);
        }
        return DoWrap.createFunctionToWrap(matchedMethod);
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
        FunctionWrapped fwAnnot = (FunctionWrapped)method.getAnnotation(FunctionWrapped.class);

        // every @FunctionWrapped annotated method in a class must have a unique value
        Preconditions.checkArgument(!namedMethods.containsKey(fwAnnot.value()),
                "Duplicate values (\"%s\") in @ annotated methods in this class", fwAnnot.value(), FunctionWrapped.class.getName());
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

        return (FunctionWrapper)field.getAnnotation(FunctionWrapper.class);
    }
}

