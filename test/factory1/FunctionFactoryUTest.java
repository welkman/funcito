package factory1;

import com.google.common.base.Function;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionFactoryUTest {

    @Test
    public void testValidateWrappers_isStatic() {
        try {
            FunctionFactory.wrap(HasNonStaticWrapperField.class);
            fail("Should have had an illegal argument exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("not static"));
        }
    }

    @Test
    public void testValidateWrappers_isFunction() {
        try {
            FunctionFactory.wrap(HasNonFunctionWrapperField.class);
            fail("Should have had an illegal argument exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("not assignable to"));
        }
    }

    @Test
    public void testSomething() {
        FunctionFactory.wrap(ToTest.class);
        ToTest me = new ToTest();

        assertEquals("This is my value", ToTest.getMyString.apply(me));
        assertEquals("other value", ToTest.getOther.apply(me));
    }

    public static class ToTest {
        static { FunctionFactory.wrap(ToTest.class);}

        @FunctionWrapper public static Function<ToTest, String> getMyString;
        @FunctionWrapper public static Function<ToTest, String> getOther;

        @FunctionWrapped
        public String getMyString() { return "This is my value"; }

        @FunctionWrapped("other")
        public String getOther() { return "other value"; }
    }

    private static class HasNonStaticWrapperField {
        @FunctionWrapper public Function<Object,Object> nonStaticFunction;
    }
    private static class HasNonFunctionWrapperField {
        @FunctionWrapper public static Object nonFunctionWrapper;
    }
}
