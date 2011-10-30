package mockStubs;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

public class MethodWrapper {
    private static MethodWrapper instance = new MethodWrapper();
    public static MethodWrapper instance() { return instance(); }

    private MethodWrapper() {};

    static final private Queue<Method> queuedMethods = new LinkedList<Method>();

    public static void pushMethod(Method method) { queuedMethods.add(method); }

    public static <T,V> Function<T,V> makeFunc(V ignoredRetVal) {
        final Method method = queuedMethods.poll();
        method.setAccessible(true);
        return new Function<T,V> () {
            public V apply(T from) {
                try {
                    return (V)method.invoke(from);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static <T,V> Predicate<T> makePred(V ignoredRetVal) {
        final Method method = queuedMethods.poll();
        method.setAccessible(true);
        return new Predicate<T>() {
            public boolean apply(T from) {
                try {
                    return ((Boolean)method.invoke(from)).booleanValue();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static <T> T stubbed(Class<T> clazz) {
        // todo: to cache this mock
        return Mockito.mock(clazz, new MethodQueingReturnValues());
    }
}
