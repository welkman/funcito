package org.funcito.internal.stub.javaproxy;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.WrapperType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

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
        Method intArgMethod = List.class.getDeclaredMethod("get", int.class);

        Object o = handler.invoke(new ArrayList<Object>(), intArgMethod, new Object[] {1});

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".List.get("));
    }

    @Test
    public void testInvoke_noArgMethodReturningNonPrimitiveNonFinal() throws Throwable {
        Method iteratorMethod = List.class.getMethod("iterator");

        Iterator<Object> i = (Iterator<Object>)handler.invoke(new ArrayList<Object>(), iteratorMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".List.iterator("));
        assertNotNull(i);
    }

    @Test
    public void testInvoke_noArgMethodReturningNonPrimitiveFinal() throws Throwable {
        Method stringMethod = Object.class.getMethod("toString");

        String s = (String)handler.invoke(aNumber, stringMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Object.toString("));
        assertNull(s); // return type String is final, may not be proxied for chaining
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = Number.class.getDeclaredMethod("intValue");

        int fakeInt = (Integer)handler.invoke(aNumber, intMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.intValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = Number.class.getDeclaredMethod("floatValue");

        float fakeFloat = (Float)handler.invoke(aNumber, floatMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.floatValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = Number.class.getDeclaredMethod("longValue");

        long fakeLong = (Long)handler.invoke(aNumber, longMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.longValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = Number.class.getDeclaredMethod("doubleValue");

        double fakeDouble = (Double)handler.invoke(aNumber, doubleMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.doubleValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = Number.class.getDeclaredMethod("shortValue");

        short fakeShort = (Short)handler.invoke(aNumber, shortMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.shortValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = Number.class.getDeclaredMethod("byteValue");

        byte fakeByte = (Byte)handler.invoke(aNumber, byteMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Number.byteValue("));
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = Class.class.getDeclaredMethod("isInterface");

        boolean fakeBoolean = (Boolean)handler.invoke(Class.class, booleanMethod, null);

        Invokable invokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertTrue(invokable.getMethodName().contains(".Class.isInterface("));
    }
}
