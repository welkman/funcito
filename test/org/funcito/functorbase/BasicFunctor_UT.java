package org.funcito.functorbase;

import com.google.common.collect.Lists;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.funcito.FuncitoGuava.callsTo;
import static org.junit.Assert.assertEquals;

public class BasicFunctor_UT {

    private final FuncitoDelegate delegate = new FuncitoDelegate();
    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString==null ? 0 : myString.length(); }
        public String toString() { return myString; }
        public StringThing plusABC() { return new StringThing(this.toString() + "ABC"); }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate.extractInvokableState(null);
        } catch (Throwable t) {}
    }

    @Test
    public void test_AssignToFunctorWithMatchingTypes() { // happy path
        CALLS_TO_STRING_THING.toString(); // prime the pump
        BasicFunctor<StringThing,String> functor = new BasicFunctor<StringThing, String>(getState());

        assertEquals("ABC", functor.applyImpl(new StringThing("ABC")));
    }

    @Test
    public void test_primitiveReturnType() {
        CALLS_TO_STRING_THING.size(); // size() return type is primitive int
        BasicFunctor<StringThing,Integer> functor = new BasicFunctor<StringThing, Integer>(getState());

        assertEquals(3, functor.applyImpl(new StringThing("ABC")).intValue());
    }

    @Test
    public void test_RepeatedUsage() {
        CALLS_TO_STRING_THING.size();
        BasicFunctor<StringThing, Integer> superTypeRet = new BasicFunctor<StringThing,Integer>(getState());

        assertEquals(3, superTypeRet.applyImpl(new StringThing("ABC")).intValue());
        assertEquals(6, superTypeRet.applyImpl(new StringThing("ABCDEF")).intValue());
    }

    @Test
    public void test_NonPrimitiveArrayRetType() {
        class NonPrimArrayRet {
            public Object[] getVal() { return new Object[] {"ABC", Integer.MAX_VALUE}; }
        }
        callsTo(NonPrimArrayRet.class).getVal();
        BasicFunctor<NonPrimArrayRet, Object[]> wrapperIntFunc = new BasicFunctor<NonPrimArrayRet, Object[]>(getState());

        assertEquals(2, wrapperIntFunc.applyImpl(new NonPrimArrayRet()).length);
    }

    @Test
    public void test_PrimitiveArrayRetType() {
        class PrimArrayRet {
            public int[] getVal() { return new int[] {1,2,3}; }
        }
        callsTo(PrimArrayRet.class).getVal();
        BasicFunctor<PrimArrayRet, int[]> wrapperIntFunc = new BasicFunctor<PrimArrayRet, int[]>(getState());

        assertEquals(3, wrapperIntFunc.applyImpl(new PrimArrayRet()).length);
    }

    @Test
    public void test_ValidateDetectsMismatchedGenericTypes() {
        callsTo(List.class).size();
        BasicFunctor<List<String>, Integer> stringListFunc = new BasicFunctor<List<String>, Integer>(getState());

        List<Integer> integerList = new ArrayList<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
        // One of these days I hope to write the code to use JDK compiler to prove that the above compiles
        // but the below does not.
//        stringListFunc.applyImpl(integerList);
    }

    @Test
    public void test_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        callsToList.get(0);
        BasicFunctor<List<String>,String> getElem0Func = new BasicFunctor<List<String>,String>(getState());
        callsToList.get(2);
        BasicFunctor<List<String>,String> getElem2Func = new BasicFunctor<List<String>,String>(getState());
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.applyImpl(list));
        assertEquals("Two", getElem2Func.applyImpl(list));
    }

    // TODO: multi-arg binding, and chained with arg binding

    @Test
    public void test_methodChain() {
        CALLS_TO_STRING_THING.plusABC().size();
        BasicFunctor<StringThing,Integer> sizePlus3Func = new BasicFunctor<StringThing,Integer>(getState());
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(6, (int) sizePlus3Func.applyImpl(origLength3));
        assertEquals(9, (int) sizePlus3Func.applyImpl(origLength6));
    }

    @Test
    public void test_methodMultiChain() {
        CALLS_TO_STRING_THING.plusABC().plusABC().size();
        BasicFunctor<StringThing,Integer> sizePlus3Plus3Func = new BasicFunctor<StringThing,Integer>(getState());
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(9, (int)sizePlus3Plus3Func.applyImpl(origLength3));
        assertEquals(12, (int) sizePlus3Plus3Func.applyImpl(origLength6));
    }

    @Test(expected = NullPointerException.class)
    public void test_MethodChainingAttemptWithUnproxyableIntermediateType() {
        // NOTE: this test proves and documents a Funcito limitation: intermediate return type String
        // is final, hence non-proxyable
        CALLS_TO_STRING_THING.toString().length();
        new BasicFunctor<StringThing,Integer>(getState());
    }

    private InvokableState getState() {
        return delegate.extractInvokableState(null);
    }
}
