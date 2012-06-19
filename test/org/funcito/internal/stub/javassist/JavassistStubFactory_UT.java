package org.funcito.internal.stub.javassist;

import org.funcito.FuncitoException;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.WrapperType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;
import java.util.Iterator;

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
public class JavassistStubFactory_UT {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private JavassistStubFactory factory = new JavassistStubFactory();

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
        thrown.expectMessage("Cannot mock");

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
    @SuppressWarnings("unchecked")
    public void testStub_BridgeMethods() throws Exception {
        FuncitoDelegate delegate = new FuncitoDelegate();

        Method bridgeMethod = C.class.getMethod("foo");
        assertTrue( bridgeMethod.isBridge());

        factory.stub(C.class).foo();
        // The main assert is just that we get past the above line without a Exception from Javassist or Funcito

        Invokable<C,String> bridgeInvokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertEquals("A", bridgeInvokable.invoke(new C()));

        // now double check method that overrides bridge method
        Method nonBridgeMethod = D.class.getMethod("foo");
        assertFalse(nonBridgeMethod.isBridge());

        factory.stub(D.class).foo();

        Invokable<D,String> nonBridgeInvokable = delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION).iterator().next();
        assertEquals("D", nonBridgeInvokable.invoke(new D()));
    }

    /**
     * This is maybe an integration test, because it depends on what the return values are for the
     * JavassistMethodHandler used internally to the JavassistStubFactory
     */
    @Test
    public void testInvoke_noExceptionForPrimitiveNumbers() {
        FuncitoDelegate delegate = new FuncitoDelegate();  //context needed for cleanup of InvocationManager
        try {
            Number numberStub = factory.stub(Number.class);

            // no NPEs means success
            numberStub.intValue();
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION); // cleanup after each
            numberStub.longValue();
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION); // etc.
            numberStub.byteValue();
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION);
            numberStub.doubleValue();
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION);
            numberStub.floatValue();
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION);
            numberStub.shortValue();
        } finally {
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION);
        }
    }

    /**
     * This is maybe an integration test, because it depends on what the return values are for the
     * JavassistMethodHandler used internally to the JavassistStubFactory
     */
    @Test
    public void testInvoke_noExceptionForPrimitiveBoolean() {
        FuncitoDelegate delegate = new FuncitoDelegate();  //context needed for cleanup of InvocationManager
        try {
            Iterator iterStub = factory.stub(Iterator.class);

            // no NPEs means success
            iterStub.hasNext();
        } finally {
            delegate.extractInvokableState(WrapperType.GUAVA_FUNCTION);
        }
    }

    interface SomeInterface {}
    enum MyEnum {}
    @Test
    public void shouldKnowIfCanImposterize() throws Exception {
        final class FinalClass {}
        class SomeClass {}
        class ClassWithConstructorThatNeedsNonNullArg {
            public ClassWithConstructorThatNeedsNonNullArg(String str) {
                str.length(); // would normally throw NPE if str was null
            }
        }

        assertFalse(factory.canImposterise(FinalClass.class));
        assertFalse(factory.canImposterise(int.class));
        assertFalse(factory.canImposterise(MyEnum.class)); // because enums are final

        assertTrue(factory.canImposterise(SomeClass.class));
        assertTrue(factory.canImposterise(SomeInterface.class));
        assertTrue(factory.canImposterise(ClassWithConstructorThatNeedsNonNullArg.class));
    }

}