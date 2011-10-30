package factory3;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

public class FunctionFactory3 {
    private static final String fieldMsgBase = "Field %s annotated with %s ";
    public static <F, T> void wrap(Class<F> clazz) {
        Iterable<Field> wrappers = findWrappers(clazz);
        validateWrapperDeclarations(wrappers);
    }

    private static void validateWrapperDeclarations(Iterable<Field> wrappers) {
        for (Field wrapper : wrappers) {
            checkArgument(Function.class.isAssignableFrom(wrapper.getType()),
                    fieldMsgBase + "is not assignable to %s", wrapper.getName(), FunctionWrapper.class.getName(), Function.class.getName());
            checkArgument(Modifier.isStatic(wrapper.getModifiers()),
                    fieldMsgBase + "is not static", wrapper.getName(), FunctionWrapper.class.getName());
        }
    }

    private static <F> Iterable<Field> findWrappers(Class<F> clazz) {
        Field[] declared = clazz.getDeclaredFields();
        Iterable<Field> wrappers = Iterables.filter(Arrays.asList(declared), new Predicate<Field>() {
            public boolean apply(Field field) {
                return field.isAnnotationPresent(FunctionWrapper.class);
            }
        });
        return wrappers;
    }
}


