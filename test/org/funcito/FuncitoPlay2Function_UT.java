package org.funcito;

import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;
import play.libs.F.Function;

import static org.funcito.FuncitoPlay2.*;
import static org.funcito.mode.Modes.safeNav;
import static org.junit.Assert.*;

public class FuncitoPlay2Function_UT {

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
            delegate().extractInvokableState(WrapperType.PLAY2_FUNCTION);
        } catch (Throwable t) {}
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() throws Throwable { // I.e. vanilla happy path
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithSourceSuperType () throws Throwable {
        Function<Object, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFuncWithTargetSuperType () throws Throwable {
        Function<StringThing, ? extends Number> superType = functionFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() throws Throwable {
        class Generic<T> {
            public Integer getVal () throws Throwable { return 123; }
        }
        class AnyOldClass{}
        Function<Generic<?>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<AnyOldClass> anyOldGeneric = new Generic<AnyOldClass>();

        assertEquals(123, stringFunc.apply(anyOldGeneric).intValue());
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported () throws Throwable {
        Function<StringThing,String> pluralFunc = functionFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.apply(dog)));
        assertEquals("dog", pluralFunc.apply(dog));
    }

    @Test
    public void testFunctionFor_TypedAndUntypedModes() throws Throwable {
        class Child {}
        class Parent {
            public Child getChild() { return null; }
        }
        Parent parent = new Parent();
        Child altChild = new Child();
        assertNull(parent.getChild());

        // Note that in parameterized version, null must be cast for compile to work.
        Function<Parent, Child> func = functionFor(callsTo(Parent.class).getChild(), safeNav(altChild));
        Function<Parent, Child> func2 = functionFor(callsTo(Parent.class).getChild(), safeNav());

        assertSame(altChild, func.apply(parent));
        assertNull(func2.apply(parent));
    }
}

