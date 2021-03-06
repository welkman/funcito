package org.funcito.internal.stub.javassist;

/**
 * Copyright 2011 Project Funcito Contributors
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class JavassistImposterizer_UT {
    private JavassistImposterizer imposterizer = JavassistImposterizer.INSTANCE;

    @Test
    public void createMockForObjectClass() {
        Object proxy = imposterizer.imposterise(new MethodHandlerStub(), Object.class);

        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(Object.class, superClass);
    }

    @Test
    public void shouldCreateMockFromInterface() throws Exception {
        SomeInterface proxy = imposterizer.imposterise(new MethodHandlerStub(), SomeInterface.class);

        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(Object.class, superClass);
    }

    @Test
    public void shouldCreateMockFromClass() throws Exception {
        ClassWithoutConstructor proxy = imposterizer.imposterise(new MethodHandlerStub(), ClassWithoutConstructor.class);

        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(ClassWithoutConstructor.class, superClass);
    }

    @Test
    public void shouldCreateMockFromClassThatRequiresNonNollConstructorArg() throws Exception {
        ClassWithConstructorThatNeedsNonNullArg proxy = imposterizer.imposterise(
                new MethodHandlerStub(), ClassWithConstructorThatNeedsNonNullArg.class);

        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(ClassWithConstructorThatNeedsNonNullArg.class, superClass);
    }

    @Test
    public void shouldCreateMockFromClassEvenWhenConstructorIsDodgy() throws Exception {
        try {
            new ClassWithDodgyConstructor();
            fail(); // just pre-asserting that this class has a dodgy constructor
        } catch (Exception e) {
        }

        ClassWithDodgyConstructor mock = imposterizer.imposterise(new MethodHandlerStub(), ClassWithDodgyConstructor.class);
        assertNotNull(mock);
    }

    @Test
    public void shouldMocksHaveDifferentInterceptors() throws Exception {
        SomeClass mockOne = imposterizer.imposterise(new MethodHandlerStub(), SomeClass.class);
        SomeClass mockTwo = imposterizer.imposterise(new MethodHandlerStub(), SomeClass.class);

        ProxyObject cglibFactoryOne = (ProxyObject) mockOne;
        ProxyObject cglibFactoryTwo = (ProxyObject) mockTwo;

        assertNotSame(cglibFactoryOne.getHandler(), cglibFactoryTwo.getHandler());
    }

    class SomeClass {}

    protected interface SomeInterface {}

    private class ClassWithConstructorThatNeedsNonNullArg {
        public ClassWithConstructorThatNeedsNonNullArg(String str) {
            str.length(); // would normally throw NPE if str was null
        }
    }

    private class ClassWithoutConstructor {}

    private class ClassWithDodgyConstructor {
        public ClassWithDodgyConstructor() {
            throw new RuntimeException();
        }
    }

    private final class MethodHandlerStub implements MethodHandler {
        public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
            return null;
        }
    }
}
