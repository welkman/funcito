package org.funcito.internal.stub.javassist;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Method;

public class JavassistMethodHandler_UT {

    private JavassistMethodHandler handler = new JavassistMethodHandler();
    private FuncitoDelegate delegate = new FuncitoDelegate();

    @After
    public void tearDown() {
        // pop last method pushed, between each test
        delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = Integer.class.getDeclaredMethod("intValue");

        // no NPEs means success
        int fakeInt = (Integer)handler.invoke(null, intMethod, null, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = Integer.class.getDeclaredMethod("floatValue");

        // no NPEs means success
        float fakeFloat = (Float)handler.invoke(null, floatMethod, null, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = Integer.class.getDeclaredMethod("longValue");

        // no NPEs means success
        long fakeLong = (Long)handler.invoke(null, longMethod, null, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = Integer.class.getDeclaredMethod("doubleValue");

        // no NPEs means success
        double fakeDouble = (Double)handler.invoke(null, doubleMethod, null, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = Integer.class.getDeclaredMethod("shortValue");

        // no NPEs means success
        short fakeShort = (Short)handler.invoke(null, shortMethod, null, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = Integer.class.getDeclaredMethod("byteValue");

        // no NPEs means success
        byte fakeByte = (Byte)handler.invoke(null, byteMethod, null, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = Class.class.getDeclaredMethod("isInterface");

        // no NPEs means success
        boolean fakeBoolean = (Boolean)handler.invoke(null, booleanMethod, null, null);
    }
}
