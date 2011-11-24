package org.funcito.cglib;

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

/* Copied in large part from project Mockito, with the following license:
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.funcito.stub.cglib.internal.CglibImposterizer;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class CglibImposterizer_UT {
    private CglibImposterizer imposterizer = CglibImposterizer.INSTANCE;

    @Test
    public void shouldCreateMockFromInterface() throws Exception {
        SomeInterface proxy = imposterizer.imposterise(new MethodInterceptorStub(), SomeInterface.class);

        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(Object.class, superClass);
    }

    @Test
    public void shouldCreateMockFromClass() throws Exception {
        ClassWithoutConstructor proxy = imposterizer.imposterise(new MethodInterceptorStub(), ClassWithoutConstructor.class);

        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(ClassWithoutConstructor.class, superClass);
    }

    @Test
    public void shouldCreateMockFromClassThatRequiresNonNollConstructorArg() throws Exception {
        ClassWithConstructorThatNeedsNonNullArg proxy = imposterizer.imposterise(
                new MethodInterceptorStub(), ClassWithConstructorThatNeedsNonNullArg.class);

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

        ClassWithDodgyConstructor mock = imposterizer.imposterise(new MethodInterceptorStub(), ClassWithDodgyConstructor.class);
        assertNotNull(mock);
    }

    @Test
    public void shouldMocksHaveDifferentInterceptors() throws Exception {
        SomeClass mockOne = imposterizer.imposterise(new MethodInterceptorStub(), SomeClass.class);
        SomeClass mockTwo = imposterizer.imposterise(new MethodInterceptorStub(), SomeClass.class);

        Factory cglibFactoryOne = (Factory) mockOne;
        Factory cglibFactoryTwo = (Factory) mockTwo;

        assertNotSame(cglibFactoryOne.getCallback(0), cglibFactoryTwo.getCallback(0));
    }

    final class FinalClass {
    }

    class SomeClass {
    }

    interface SomeInterface {
    }

    @Test
    public void shouldKnowIfCanImposterize() throws Exception {
        assertFalse(imposterizer.canImposterise(FinalClass.class));
        assertFalse(imposterizer.canImposterise(int.class));

        assertTrue(imposterizer.canImposterise(SomeClass.class));
        assertTrue(imposterizer.canImposterise(SomeInterface.class));
        assertTrue(imposterizer.canImposterise(ClassWithConstructorThatNeedsNonNullArg.class));
    }

    private class ClassWithConstructorThatNeedsNonNullArg {
        public ClassWithConstructorThatNeedsNonNullArg(String str) {
            str.length(); // would normally throw NPE if str was null
        }
    }

    private class ClassWithoutConstructor {
    }

    private class ClassWithDodgyConstructor {
        public ClassWithDodgyConstructor() {
            throw new RuntimeException();
        }
    }

    private final class MethodInterceptorStub implements MethodInterceptor {

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return null;
        }
    }
}
