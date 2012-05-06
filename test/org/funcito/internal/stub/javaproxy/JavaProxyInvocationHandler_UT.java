package org.funcito.internal.stub.javaproxy;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.WrapperType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

/*
 * Copyright 2012 Project Funcito Contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class JavaProxyInvocationHandler_UT {

    private JavaProxyInvocationHandler handler = new JavaProxyInvocationHandler();
    private FuncitoDelegate delegate = new FuncitoDelegate();
    private Number aNumber = 1;

    @Test
    public void testInvoke_methodWithArgReturningNonPrimitive() throws Throwable {
        Method intMethod = List.class.getDeclaredMethod("get", int.class);

        Object o = handler.invoke(aNumber, intMethod, new Object[] {1});

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("get", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noArgMethodReturningNonPrimitive() throws Throwable {
        Method intMethod = Object.class.getDeclaredMethod("getClass");

        Class c = (Class)handler.invoke(aNumber, intMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("getClass", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = Number.class.getDeclaredMethod("intValue");

        int fakeInt = (Integer)handler.invoke(aNumber, intMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("intValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = Number.class.getDeclaredMethod("floatValue");

        float fakeFloat = (Float)handler.invoke(aNumber, floatMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("floatValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = Number.class.getDeclaredMethod("longValue");

        long fakeLong = (Long)handler.invoke(aNumber, longMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("longValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = Number.class.getDeclaredMethod("doubleValue");

        double fakeDouble = (Double)handler.invoke(aNumber, doubleMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("doubleValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = Number.class.getDeclaredMethod("shortValue");

        short fakeShort = (Short)handler.invoke(aNumber, shortMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("shortValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = Number.class.getDeclaredMethod("byteValue");

        byte fakeByte = (Byte)handler.invoke(aNumber, byteMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("byteValue", invokable.getMethodName());
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = Class.class.getDeclaredMethod("isInterface");

        boolean fakeBoolean = (Boolean)handler.invoke(Class.class, booleanMethod, null);

        Invokable invokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("isInterface", invokable.getMethodName());
    }
}
