package org.funcito.internal.stub.cglib;

import org.funcito.FuncitoException;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.WrapperType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

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
public class CglibStubFactory_UT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CglibStubFactory factory = new CglibStubFactory();

    @Test
    public void testStub_CachesInstancesOfSameClass() {
        class MyClass {
        }
        MyClass inst1 = factory.stub(MyClass.class);
        MyClass inst2 = factory.stub(MyClass.class);

        assertSame(inst1, inst2);
    }

    @Test
    public void testStub_UnstubbableClassesThrowFuncitoException() {
        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Cannot proxy");

        factory.stub(String.class); // String is final, should not be stubbable
    }

    static class A {
        public String foo() { return "A"; }
    }
    public static class C extends A {
        // C generates a bridge "foo" method because class D is more visible class
        // see http://stas-blogspot.blogspot.com/2010/03/java-bridge-methods-explained.html
    }
    public static class D extends A {
        public String foo() { return "D"; }
    }

    @Test
    public void testStub_BridgeMethods() throws Exception {
        FuncitoDelegate delegate = new FuncitoDelegate();

        Method bridgeMethod = C.class.getMethod("foo");
        assertTrue( bridgeMethod.isBridge());

        factory.stub(C.class).foo();
        // The main assert is just that we get past the above line without an IllegalArgumentException from Cglib

        Invokable<C,String> bridgeInvokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("A", bridgeInvokable.invoke(new C()));

        // now double check method that overrides bridge method
        Method nonBridgeMethod = D.class.getMethod("foo");
        assertFalse(nonBridgeMethod.isBridge());

        factory.stub(D.class).foo();

        Invokable<D,String> nonBridgeInvokable = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertEquals("D", nonBridgeInvokable.invoke(new D()));
    }

}
