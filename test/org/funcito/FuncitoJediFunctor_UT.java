package org.funcito;

import com.google.common.collect.Lists;
import jedi.functional.Functor;
import org.junit.Test;

import java.util.List;

import static org.funcito.FuncitoJedi.*;
import static org.junit.Assert.*;

public class FuncitoJediFunctor_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private static class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
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
    public void testFunctorFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return 123; }
        }
        Functor<IntegerWrapperRet, Integer> wrapperIntFunc = functorFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.execute(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctorFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        Functor<PrimitiveIntRet, Integer> primIntFunc = functorFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.execute(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctorFor_ValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        Functor<Generic<String>, Integer> stringFunc = functorFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.apply(integerGeneric);
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
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        Functor<StringThing,String> pluralFunc = functorFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.execute(dog)));
        assertEquals("dog", pluralFunc.execute(dog));
    }

    @Test
    public void testFunctorFor_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        Functor<List<String>,String> getElem0Func = functorFor(callsToList.get(0));
        Functor<List<String>,String> getElem2Func = functorFor(callsToList.get(2));
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.execute(list));
        assertEquals("Two", getElem2Func.execute(list));
    }
}

