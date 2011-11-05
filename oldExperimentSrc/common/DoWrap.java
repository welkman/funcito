package common;

import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class DoWrap {
    public static <F, T> Function<F, T> createFunctionToWrap(final Method method) {
        return new Function<F, T>() {
            public T apply(@Nullable F f) {
                try {
                    return (T) method.invoke(f);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
