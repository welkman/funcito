package org.funcito.internal.stub.javaproxy;

import org.funcito.FuncitoException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertSame;

public class JavaProxyStubFactory_UT {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private JavaProxyStubFactory factory = new JavaProxyStubFactory();

    interface MyInterface { }

    @Test
    public void testStub_CachesInstancesOfSameClass() {
        MyInterface inst1 = factory.stub(MyInterface.class);
        MyInterface inst2 = factory.stub(MyInterface.class);

        assertSame(inst1, inst2);
    }

    @Test
    public void testStub_UnstubbableClassesThrowFuncitoException() {
        class MyClass {
        }
        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Cannot proxy");

        factory.stub(MyClass.class); // MyClass is class, should not be stubbable
    }
}
