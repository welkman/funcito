package org.funcito;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.funcito.internal.WrapperType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.funcito.FuncitoGuava.*;
import static org.junit.Assert.*;

public class FuncitoGuavaFunction_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
        public StringThing plusABC() { return new StringThing(this.toString() + "ABC"); }
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() { // I.e. vanilla happy path
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_RepeatedUsage() {
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
        assertEquals(6, superTypeRet.apply(new StringThing("ABCDEF")).intValue());
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
    public void testFunctionFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return 123; }
        }
        Function<IntegerWrapperRet, Integer> wrapperIntFunc = functionFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.apply(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        Function<PrimitiveIntRet, Integer> primIntFunc = functionFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.apply(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctionFor_ValidateDetectsMismatchedGenericTypes() {
        Function<List<String>, Integer> stringListFunc = functionFor(callsTo(List.class).size());
        List<Integer> integerList = new ArrayList<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
        // One of these days I hope to write the code to use JDK compiler to prove that the above compiles
        // but the below does not.
//        stringListFunc.apply(integerList);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        Function<Generic<? extends Object>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.apply(integerGeneric).intValue());
    }

    @Test
    public void testFunctionFor_MethodChainingAttemptWithUnproxyableInterimType() {
        try {
            // NOTE: this test is a test that proves and documents a limitation of Funcito
            // It may be possible to eliminate this restriction where each element in the chain is proxyable.
            functionFor(CALLS_TO_STRING_THING.toString().length());
            fail("Should have thrown NPE");
        } catch (NullPointerException e) {
            // cleanup aftermath of test
            delegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        }

    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        Function<StringThing,String> pluralFunc = functionFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.apply(dog)));
        assertEquals("dog", pluralFunc.apply(dog));
    }

    @Test
    public void testFunctionFor_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        Function<List<String>,String> getElem0Func = functionFor(callsToList.get(0));
        Function<List<String>,String> getElem2Func = functionFor(callsToList.get(2));
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.apply(list));
        assertEquals("Two", getElem2Func.apply(list));
    }

    @Test
    public void testFunctionFor_methodChain() {
        Function<StringThing,Integer> f = functionFor(CALLS_TO_STRING_THING.plusABC().size());
        StringThing before = new StringThing("XYZ");

        int afterSize = f.apply(before);

        assertEquals(6, afterSize);
    }

    @Test
    public void testFunctionFor_methodChainMultipleCalls() {
        Function<StringThing,Integer> f = functionFor(CALLS_TO_STRING_THING.plusABC().size());
        StringThing before1 = new StringThing("XYZ");
        StringThing before2 = new StringThing("XYZXYZ");

        int afterSize = f.apply(before1);

        assertEquals(6, afterSize);

        afterSize = f.apply(before2);

        assertEquals(9, afterSize);
    }
}

