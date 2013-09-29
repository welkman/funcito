package org.funcito;

import com.google.common.base.Function;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import static org.funcito.FuncitoGuava.*;
import static org.funcito.mode.SafeNav.SAFE_NAV;
import static org.funcito.mode.Modes.*;
import static org.junit.Assert.*;

public class FuncitoGuavaFunction_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString==null ? 0 : myString.length(); }
        public String toString() { return myString; }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    // TODO: rename as happy path in all API UTs
    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() { // I.e. vanilla happy path
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithSourceSuperType() {
        Function<Object, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFuncWithTargetSuperType() {
        Function<StringThing, ? extends Number> superType = functionFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        class AnyOldClass{}
        Function<Generic<?>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<AnyOldClass> anyOldGeneric = new Generic<AnyOldClass>();

        assertEquals(123, stringFunc.apply(anyOldGeneric).intValue());
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        // badly formed Funcito function: the "+" operator cannot be captured
        Function<StringThing,String> pluralFunc = functionFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.apply(dog)));
        assertEquals("dog", pluralFunc.apply(dog));
    }


    @Test
    public void testFunctionFor_SafeNav_NoChainNullDefault() {
        class Child {}
        class Parent {
            public Child getChild() { return null; }
        }
        Parent parent = new Parent();
        assertNull(parent.getChild());

        // Note that in parameterized version, null must be cast for compile to work.
        Function<Parent, Child> func = functionFor(callsTo(Parent.class).getChild(), safeNav((Child)null));
        Function<Parent, Child> func2 = functionFor(callsTo(Parent.class).getChild(), safeNav());
        Function<Parent, Child> func3 = functionFor(callsTo(Parent.class).getChild(), SAFE_NAV);

        assertNull(func.apply(parent));
        assertNull(func2.apply(parent));
        assertNull(func3.apply(parent));
    }
}

