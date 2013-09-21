package org.funcito.functorbase;

import com.google.common.collect.Lists;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This is a base class for testing other FunctorBase implementation classes
 */
abstract public class FunctorBaseTestBase {

    protected final FuncitoDelegate delegate = new FuncitoDelegate();
    private StringThing CALLS_TO_STRING_THING = delegate.callsTo(StringThing.class);

    class StringThing {
        protected String s1, s2;

        public StringThing(String s1) { this.s1 = s1; }
        public StringThing(String s1, String s2) { this.s1 = s1; this.s2 = s2; }
        public int size() { return s1 ==null ? 0 : s1.length(); }
        public String toString() { return s1; }
        public StringThing plusABC() { return new StringThing(this.toString() + "ABC"); }
        public boolean sameLength() { return s1.length() == s2.length(); }
        public Boolean s1Empty() { return s1==null || s1.isEmpty(); }
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
        CALLS_TO_STRING_THING.s1Empty(); // prime the pump
        FunctorBase<StringThing,Boolean> functor = makeFunctorForTest();

        assertTrue(functor.applyImpl(new StringThing(null)));
    }

    @Test
    public void test_primitiveReturnType() {
        CALLS_TO_STRING_THING.size(); // size() return type is primitive int
        FunctorBase<StringThing,Integer> functor = makeFunctorForTest();

        assertEquals(3, functor.applyImpl(new StringThing("ABC")).intValue());
    }

    @Test
    public void test_RepeatedUsage() {
        CALLS_TO_STRING_THING.sameLength();
        FunctorBase<StringThing, Boolean> functor = makeFunctorForTest();

        assertTrue(functor.applyImpl(new StringThing("ABC", "XYZ")));
        assertFalse(functor.applyImpl(new StringThing("ABC", "XYZ2")));
    }

    @Test
    public void test_ValidateDetectsMismatchedGenericTypes() {
        delegate.callsTo(List.class).isEmpty();
        FunctorBase<List<String>, Boolean> stringListFunc = makeFunctorForTest();

        List<Integer> integerList = new ArrayList<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
        // One of these days I hope to write the code to use JDK compiler to prove that the above compiles
        // but the below does not.
//        stringListFunc.applyImpl(integerList);
    }

    @Test
    public void test_SingleArgBinding() {
        List<Boolean> callsToList = delegate.callsTo(List.class);
        callsToList.get(0);
        FunctorBase<List<Boolean>,Boolean> getElem0Func = makeFunctorForTest();
        callsToList.get(3);
        FunctorBase<List<Boolean>,Boolean> getElem3Func = makeFunctorForTest();
        List<Boolean> list = Lists.newArrayList(true, true, true, false);

        assertTrue(getElem0Func.applyImpl(list));
        assertFalse(getElem3Func.applyImpl(list));
    }

    // TODO, need to make all functors in all tests here return Boolean, so that this can be used as base test
    // for predicates as well

    @Test
    public void test_MultiArgBinding() {
        class MyMath {
            private int val;
            public MyMath(int val) { this.val = val; }
            public Boolean between(int low, int high) { return (val>=low) && (val<=high); }
        }

        delegate.callsTo(MyMath.class).between(3, 5);
        FunctorBase<MyMath,Boolean> functor = makeFunctorForTest();

        assertFalse(functor.applyImpl(new MyMath(2)));
        assertTrue(functor.applyImpl(new MyMath(3)));
        assertTrue(functor.applyImpl(new MyMath(4)));
        assertTrue(functor.applyImpl(new MyMath(5)));
        assertFalse(functor.applyImpl(new MyMath(6)));
    }

    @Test
    public void test_methodChain() {
        CALLS_TO_STRING_THING.plusABC().size();
        FunctorBase<StringThing,Integer> sizePlus3Func = makeFunctorForTest();
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(6, (int) sizePlus3Func.applyImpl(origLength3));
        assertEquals(9, (int) sizePlus3Func.applyImpl(origLength6));
    }

    @Test
    public void test_methodMultiChain() {
        CALLS_TO_STRING_THING.plusABC().plusABC().size();
        FunctorBase<StringThing,Integer> sizePlus3Plus3Func = makeFunctorForTest();
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(9, (int)sizePlus3Plus3Func.applyImpl(origLength3));
        assertEquals(12, (int) sizePlus3Plus3Func.applyImpl(origLength6));
    }

    @Test(expected = NullPointerException.class)
    public void test_MethodChainingAttemptWithUnproxyableIntermediateType() {
        // NOTE: this test proves and documents a Funcito limitation: intermediate return type String
        // is final, hence non-proxyable
        CALLS_TO_STRING_THING.toString().isEmpty();
        makeFunctorForTest();
    }

    abstract protected <T,V> FunctorBase<T, V> makeFunctorForTest();

    protected InvokableState getState() {
        return delegate.extractInvokableState(null);
    }
}
