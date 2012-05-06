package org.funcito.internal.stub.javassist;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.WrapperType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JavassistMethodHandler_UT {

    private JavassistMethodHandler handler = new JavassistMethodHandler();
    private FuncitoDelegate delegate = new FuncitoDelegate();
    private Number aNumber = 1;

    @Test
    public void testInvoke_methodWithArgReturningNonPrimitive() throws Throwable {
        Method intMethod = List.class.getDeclaredMethod("get", int.class);

        Object o = handler.invoke(aNumber, intMethod, null, new Object[] {1});

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("get", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noArgMethodReturningNonPrimitive() throws Throwable {
        Method intMethod = Object.class.getDeclaredMethod("getClass");

        Class c = (Class)handler.invoke(aNumber, intMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("getClass", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = Number.class.getDeclaredMethod("intValue");

        int fakeInt = (Integer)handler.invoke(aNumber, intMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("intValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = Number.class.getDeclaredMethod("floatValue");

        float fakeFloat = (Float)handler.invoke(aNumber, floatMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("floatValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = Number.class.getDeclaredMethod("longValue");

        long fakeLong = (Long)handler.invoke(aNumber, longMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("longValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = Number.class.getDeclaredMethod("doubleValue");

        double fakeDouble = (Double)handler.invoke(aNumber, doubleMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("doubleValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = Number.class.getDeclaredMethod("shortValue");

        short fakeShort = (Short)handler.invoke(aNumber, shortMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("shortValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = Number.class.getDeclaredMethod("byteValue");

        byte fakeByte = (Byte)handler.invoke(aNumber, byteMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("byteValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = Class.class.getDeclaredMethod("isInterface");

        boolean fakeBoolean = (Boolean)handler.invoke(Class.class, booleanMethod, null, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("isInterface", invokable.getMethodName());
    }
}
