package org.funcito;

import fj.F;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import static org.funcito.FuncitoFJ.*;
import static org.funcito.mode.Modes.safeNav;
import static org.junit.Assert.*;

public class FuncitoFjF_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.FJ_F);
        } catch (Throwable t) {}
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() { // I.e. vanilla happy path
        F<StringThing, Integer> superTypeRet = fFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.f(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithSourceSuperType() {
        F<Object, Integer> superTypeRet = fFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.f(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFuncWithTargetSuperType() {
        F<StringThing, ? extends Number> superType = fFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.f(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        F<Generic<?>, Integer> stringFunc = fFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.f(integerGeneric).intValue());
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        F<StringThing,String> pluralFunc = fFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.f(dog)));
        assertEquals("dog", pluralFunc.f(dog));
    }

    @Test
    public void testFunctionFor_SafeNav_NoChainNullDefault() {
        class Child {}
        class Parent {
            public Child getChild() { return null; }
        }
        Parent parent = new Parent();
        Child altChild = new Child();
        assertNull(parent.getChild());

        // Note that in parameterized version, null must be cast for compile to work.
        F<Parent, Child> func = fFor(callsTo(Parent.class).getChild(), safeNav(altChild));
        F<Parent, Child> func2 = fFor(callsTo(Parent.class).getChild(), safeNav());

        assertSame(altChild, func.f(parent));
        assertNull(func2.f(parent));
    }
}

