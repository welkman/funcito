package org.funcito.internal.stub.javaproxy;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Method;

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
    private Integer anInt = 1;

    @After
    public void tearDown() {
        // pop last method pushed, between each test
        delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveInt() throws Throwable {
        Method intMethod = Integer.class.getDeclaredMethod("intValue");

        // no NPEs means success
        int fakeInt = (Integer)handler.invoke(anInt, intMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveFloat() throws Throwable {
        Method floatMethod = Integer.class.getDeclaredMethod("floatValue");

        // no NPEs means success
        float fakeFloat = (Float)handler.invoke(anInt, floatMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveLong() throws Throwable {
        Method longMethod = Integer.class.getDeclaredMethod("longValue");

        // no NPEs means success
        long fakeLong = (Long)handler.invoke(anInt, longMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveDouble() throws Throwable {
        Method doubleMethod = Integer.class.getDeclaredMethod("doubleValue");

        // no NPEs means success
        double fakeDouble = (Double)handler.invoke(anInt, doubleMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveShort() throws Throwable {
        Method shortMethod = Integer.class.getDeclaredMethod("shortValue");

        // no NPEs means success
        short fakeShort = (Short)handler.invoke(anInt, shortMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveByte() throws Throwable {
        Method byteMethod = Integer.class.getDeclaredMethod("byteValue");

        // no NPEs means success
        byte fakeByte = (Byte)handler.invoke(anInt, byteMethod, null);
    }

    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() throws Throwable {
        Method booleanMethod = Class.class.getDeclaredMethod("isInterface");

        // no NPEs means success
        boolean fakeBoolean = (Boolean)handler.invoke(Class.class, booleanMethod, null);
    }

}
