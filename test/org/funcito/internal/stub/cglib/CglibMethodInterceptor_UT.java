package org.funcito.internal.stub.cglib;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.WrapperType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CglibMethodInterceptor_UT {
    private CglibMethodInterceptor handler = new CglibMethodInterceptor();
    private FuncitoDelegate delegate = new FuncitoDelegate();
    private Number aNumber = 1;

    @Test
    public void testInvoke_methodWithArgReturningNonPrimitive() throws Throwable {
        Method intMethod = List.class.getDeclaredMethod("get", int.class);

        Object o = handler.intercept(aNumber, intMethod, new Object[] {1}, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".List.get("));
    }

    @Test
    public void testInvoke_noArgMethodReturningNonPrimitive() throws Throwable {
        Method intMethod = Object.class.getDeclaredMethod("getClass");

        Class c = (Class)handler.intercept(aNumber, intMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Object.getClass("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = Number.class.getDeclaredMethod("intValue");

        int fakeInt = (Integer)handler.intercept(aNumber, intMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.intValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = Number.class.getDeclaredMethod("floatValue");

        float fakeFloat = (Float)handler.intercept(aNumber, floatMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.floatValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = Number.class.getDeclaredMethod("longValue");

        long fakeLong = (Long)handler.intercept(aNumber, longMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.longValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = Number.class.getDeclaredMethod("doubleValue");

        double fakeDouble = (Double)handler.intercept(aNumber, doubleMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.doubleValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = Number.class.getDeclaredMethod("shortValue");

        short fakeShort = (Short)handler.intercept(aNumber, shortMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.shortValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = Number.class.getDeclaredMethod("byteValue");

        byte fakeByte = (Byte)handler.intercept(aNumber, byteMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.byteValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = Class.class.getDeclaredMethod("isInterface");

        boolean fakeBoolean = (Boolean)handler.intercept(Class.class, booleanMethod, null, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Class.isInterface("));
    }
}
