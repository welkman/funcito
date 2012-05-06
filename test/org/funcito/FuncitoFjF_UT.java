package org.funcito;

import com.google.common.collect.Lists;
import fj.F;
import org.junit.Test;

import java.util.List;

import static org.funcito.FuncitoFJ.*;
import static org.junit.Assert.*;

public class FuncitoFjF_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
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
    public void testFunctionFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return 123; }
        }
        F<IntegerWrapperRet, Integer> wrapperIntFunc = fFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.f(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        F<PrimitiveIntRet, Integer> primIntFunc = fFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.f(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctionFor_ValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        F<Generic<String>, Integer> stringFunc = fFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.f(integerGeneric);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        F<Generic<? extends Object>, Integer> stringFunc = fFor(callsTo(Generic.class).getVal());
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
    public void testFunctionFor_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        F<List<String>,String> getElem0Func = fFor(callsToList.get(0));
        F<List<String>,String> getElem2Func = fFor(callsToList.get(2));
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.f(list));
        assertEquals("Two", getElem2Func.f(list));
    }

}

