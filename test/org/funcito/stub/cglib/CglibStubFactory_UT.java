package org.funcito.stub.cglib;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.cglib.CglibStubFactory;
import org.junit.Test;

import static org.junit.Assert.assertSame;

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

    @Test
    public void testStub_CachesInstancesOfSameClass() {
        CglibStubFactory factory = new CglibStubFactory();

        MyClass inst1 = factory.stub(MyClass.class);
        MyClass inst2 = factory.stub(MyClass.class);

        assertSame(inst1, inst2);
    }

    @Test(expected = FuncitoException.class)
    public void testStub_UnstubbableClassesThrowFuncitoException() {
        CglibStubFactory factory = new CglibStubFactory();

        factory.stub(String.class); // String is final, should not be stubbable
    }

    class MyClass {
    }
}
