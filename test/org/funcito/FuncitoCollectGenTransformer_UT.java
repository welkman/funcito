package org.funcito;

import com.google.common.collect.Lists;
import org.apache.commons.collections15.Transformer;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.funcito.FuncitoCollectGen.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FuncitoCollectGenTransformer_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
        public StringThing plusABC() { return new StringThing(this.toString() + "ABC"); }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    @Test
    public void testTransformerFor_AssignToTransformerWithMatchingTypes() { // I.e. vanilla happy path
        Transformer<StringThing, Integer> superTypeRet = transformerFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.transform(new StringThing("ABC")).intValue());
    }

    @Test
    public void testTransformerFor_RepeatedUsage() {
        Transformer<StringThing, Integer> superTypeRet = transformerFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.transform(new StringThing("ABC")).intValue());
        assertEquals(6, superTypeRet.transform(new StringThing("ABCDEF")).intValue());
    }

    @Test
    public void testTransformerFor_AssignToTransformerWithSourceSuperType() {
        Transformer<Object, Integer> superTypeRet = transformerFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.transform(new StringThing("ABC")).intValue());
    }

    @Test
    public void testTransformerFor_AssignToFuncWithTargetSuperType() {
        Transformer<StringThing, ? extends Number> superType = transformerFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.transform(thing);
        assertEquals(6, n);
    }

    @Test
    public void testTransformerFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return 123; }
        }
        Transformer<IntegerWrapperRet, Integer> wrapperIntFunc = transformerFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.transform(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testTransformerFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        Transformer<PrimitiveIntRet, Integer> primIntFunc = transformerFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.transform(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testTransformerFor_MethodHasNonPrimitiveArrayRetType() {
        class NonPrimArrayRet {
            public Object[] getVal() { return new Object[] {"ABC", Integer.MAX_VALUE}; }
        }
        Transformer<NonPrimArrayRet, Object[]> wrapperIntFunc = transformerFor(callsTo(NonPrimArrayRet.class).getVal());
        assertEquals(2, wrapperIntFunc.transform(new NonPrimArrayRet()).length);
    }

    @Test
    public void testTransformerFor_MethodHasPrimitiveArrayRetType() {
        class PrimArrayRet {
            public int[] getVal() { return new int[] {1,2,3}; }
        }
        Transformer<PrimArrayRet, int[]> wrapperIntFunc = transformerFor(callsTo(PrimArrayRet.class).getVal());
        assertEquals(3, wrapperIntFunc.transform(new PrimArrayRet()).length);
    }

    @Test
    public void testTransformerFor_ValidateDetectsMismatchedGenericTypes() {
        Transformer<List<String>, Integer> stringListFunc = transformerFor(callsTo(List.class).size());
        List<Integer> integerList = new ArrayList<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
        // One of these days I hope to write the code to use JDK compiler to prove that the above compiles
        // but the below does not.
//        stringListFunc.transform(integerList);
    }

    @Test
    public void testTransformerFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        class AnyOldClass{}
        Transformer<Generic<?>, Integer> stringFunc = transformerFor(callsTo(Generic.class).getVal());
        Generic<AnyOldClass> anyOldGeneric = new Generic<AnyOldClass>();

        assertEquals(123, stringFunc.transform(anyOldGeneric).intValue());
    }

    @Test
    public void testTransformerFor_ExpressionsWithOperatorsAreUnsupported() {
        Transformer<StringThing,String> pluralFunc = transformerFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.transform(dog)));
        assertEquals("dog", pluralFunc.transform(dog));
    }

    @Test
    public void testTransformerFor_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        Transformer<List<String>,String> getElem0Func = transformerFor(callsToList.get(0));
        Transformer<List<String>,String> getElem2Func = transformerFor(callsToList.get(2));
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.transform(list));
        assertEquals("Two", getElem2Func.transform(list));
    }

    @Test
    public void testTransformerFor_methodChain() {
        Transformer<StringThing,Integer> sizePlus3Func = transformerFor(CALLS_TO_STRING_THING.plusABC().size());
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(6, (int)sizePlus3Func.transform(origLength3));
        assertEquals(9, (int) sizePlus3Func.transform(origLength6));
    }

    @Test
    public void testTransformerFor_methodMultiChain() {
        Transformer<StringThing,Integer> sizePlus3Plus3Func = transformerFor(CALLS_TO_STRING_THING.plusABC().plusABC().size());
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(9, (int)sizePlus3Plus3Func.transform(origLength3));
        assertEquals(12, (int) sizePlus3Plus3Func.transform(origLength6));
    }

    @Test(expected = NullPointerException.class)
    public void testTransformerFor_MethodChainingAttemptWithUnproxyableInterimType() {
        // NOTE: this test is a test that proves and documents a limitation of Funcito: interim type String
        // is final, hence non-proxyable
        transformerFor(CALLS_TO_STRING_THING.toString().length());
    }
}

