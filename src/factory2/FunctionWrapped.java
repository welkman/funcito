package factory2;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FunctionWrapped {
    public String value() default "";
}
