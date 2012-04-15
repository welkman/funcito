package org.funcito.internal.stub.javaproxy;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNull;

public class JavaProxyInvocationHandler_UT {

    private JavaProxyInvocationHandler handler = new JavaProxyInvocationHandler();
    private FuncitoDelegate delegate = new FuncitoDelegate();

    private interface MyInterface{
        short getShort();
        int getInt();
        long getLong();
        float getFloat();
        double getDouble();
        boolean getBool();
        byte getByte();
        Object getObj();
    }
    MyInterface impl = new MyInterface() {
        public short getShort() { return 0;}
        public int getInt() { return 0; }
        public long getLong() { return 0; }
        public float getFloat() { return 0; }
        public double getDouble() { return 0; }
        public boolean getBool() { return false; }
        public byte getByte() { return 0; }
        public Object getObj() { return null; }
    };

    @After
    public void tearDown() {
        // pop last method pushed, between each test
        delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = MyInterface.class.getDeclaredMethod("getInt");

        // no NPEs means success
        int fakeInt = (Integer)handler.invoke(impl, intMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = MyInterface.class.getDeclaredMethod("getFloat");

        // no NPEs means success
        float fakeFloat = (Float)handler.invoke(impl, floatMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = MyInterface.class.getDeclaredMethod("getLong");

        // no NPEs means success
        long fakeLong = (Long)handler.invoke(impl, longMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = MyInterface.class.getDeclaredMethod("getDouble");

        // no NPEs means success
        double fakeDouble = (Double)handler.invoke(impl, doubleMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = MyInterface.class.getDeclaredMethod("getShort");

        // no NPEs means success
        short fakeShort = (Short)handler.invoke(impl, shortMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = MyInterface.class.getDeclaredMethod("getByte");

        // no NPEs means success
        byte fakeByte = (Byte)handler.invoke(impl, byteMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = MyInterface.class.getDeclaredMethod("getBool");

        // no NPEs means success
        boolean fakeBoolean = (Boolean)handler.invoke(impl, booleanMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForObject() throws Throwable {
        Method objMethod = MyInterface.class.getDeclaredMethod("getObj");

        Object fakeObj = handler.invoke(impl, objMethod, null);

        assertNull(fakeObj);
    }
}
