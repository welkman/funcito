package org.funcito;

/**
 * Copyright 2013 Project Funcito Contributors
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

import jedi.functional.Functor;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import static org.funcito.FuncitoJedi.*;
import static org.funcito.mode.Modes.safeNav;
import static org.junit.Assert.*;

public class FuncitoJediFunctor_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private static class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.JEDI_FUNCTOR);
        } catch (Throwable t) {}
    }

    @Test
    public void testFunctorFor_AssignToFunctorWithMatchingTypes() {
        Functor<StringThing, Integer> superTypeRet = functorFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.execute(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctorFor_AssignToFunctorWithSourceSuperType() {
        Functor<Object, Integer> superTypeRet = functorFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.execute(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctorFor_AssignToFuncWithTargetSuperType() {
        Functor<StringThing, ? extends Number> superType = functorFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.execute(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctorFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        Functor<Generic<? extends Object>, Integer> stringFunc = functorFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.execute(integerGeneric).intValue());
    }

    @Test
    public void testFunctorFor_ExpressionsWithOperatorsAreUnsupported() {
        Functor<StringThing,String> pluralFunc = functorFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.execute(dog)));
        assertEquals("dog", pluralFunc.execute(dog));
    }

    @Test
    public void testFunctorFor_TypedAndUntypedModes() {
        class Child {}
        class Parent {
            public Child getChild() { return null; }
        }
        Parent parent = new Parent();
        Child altChild = new Child();
        assertNull(parent.getChild());

        // Note that in parameterized version, null must be cast for compile to work.
        Functor<Parent, Child> func = functorFor(callsTo(Parent.class).getChild(), safeNav(altChild));
        Functor<Parent, Child> func2 = functorFor(callsTo(Parent.class).getChild(), safeNav());

        assertSame(altChild, func.execute(parent));
        assertNull(func2.execute(parent));
    }
}

