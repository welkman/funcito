package org.funcito.internal.stub.javaproxy;

import org.funcito.FuncitoException;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
public class JavaProxyStubFactory_UT {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private JavaProxyStubFactory factory = new JavaProxyStubFactory();

    interface NumberInterface {
        public int intValue();
        public long longValue();
        public float floatValue();
        public double doubleValue();
        public byte byteValue();
        public short shortValue();
    }

    @Test
    public void testStub_CachesInstancesOfSameClass() {
        NumberInterface inst1 = factory.stub(NumberInterface.class);
        NumberInterface inst2 = factory.stub(NumberInterface.class);

        assertSame(inst1, inst2);
    }

    @Test
    public void testStub_UnstubbableClassesThrowFuncitoException() {
        class MyClass {
        }
        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Cannot proxy");

        factory.stub(MyClass.class); // MyClass is not interface, should not be stubbable by Java Proxy
    }

    /**
     * This is maybe an integration test, because it depends on what the return values are for the
     * JavaProxyMethodHandler used internally to the JavaProxyStubFactory
     */
    @Test
    public void testInvoke_noExceptionForPrimitiveNumbers() {
        FuncitoDelegate delegate = new FuncitoDelegate();  //context needed for cleanup of InvocationManager
        try {
            NumberInterface numberStub = factory.stub(NumberInterface.class);

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
     * JavaProxyMethodHandler used internally to the JavaProxyStubFactory
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
        class NonFinalClass {}

        assertFalse(factory.canImposterise(int.class));
        assertFalse(factory.canImposterise(MyEnum.class));
        assertFalse(factory.canImposterise(FinalClass.class));
        assertFalse(factory.canImposterise(NonFinalClass.class));
        assertFalse(factory.canImposterise(Object[].class));
        assertFalse(factory.canImposterise(int[].class));

        assertTrue(factory.canImposterise(SomeInterface.class));
    }

}
