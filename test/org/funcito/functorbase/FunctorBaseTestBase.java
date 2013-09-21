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
 * This is a base class for testing other FunctorBase implementation classes.
 * NOTE: that all Functors in these tests return Boolean, so that this base class may be used for testing Predicates.
 */
abstract public class FunctorBaseTestBase {

    protected final FuncitoDelegate delegate = new FuncitoDelegate();
    private StringThing CALLS_TO_STRING_THING = delegate.callsTo(StringThing.class);

    class StringThing {
        protected String s1;

        public StringThing(String s1) { this.s1 = s1; }
        public String toString() { return s1; }
        public StringThing chop() { return new StringThing(toString().substring(1)); }
        public Boolean empty() { return s1==null || s1.isEmpty(); }
        public boolean emptyPrim() { return empty(); }
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
        CALLS_TO_STRING_THING.empty(); // prime the pump
        FunctorBase<StringThing,Boolean> functor = makeFunctorForTest();

        assertTrue(functor.applyImpl(new StringThing(null)));
    }

    @Test
    public void test_primitiveReturnType() {
        CALLS_TO_STRING_THING.emptyPrim(); // return type is primitive boolean
        FunctorBase<StringThing,Boolean> functor = makeFunctorForTest();

        assertFalse(functor.applyImpl(new StringThing("ABC")));
    }

    @Test
    public void test_RepeatedUsage() {
        CALLS_TO_STRING_THING.empty();
        FunctorBase<StringThing, Boolean> functor = makeFunctorForTest();

        assertTrue(functor.applyImpl(new StringThing("")));
        assertFalse(functor.applyImpl(new StringThing("A")));
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
    public void test_methodSingleChain() {
        CALLS_TO_STRING_THING.chop().empty();
        FunctorBase<StringThing,Boolean> chopEmpty = makeFunctorForTest();
        StringThing origLength1 = new StringThing("1");
        StringThing origLength2 = new StringThing("12");

        assertTrue(chopEmpty.applyImpl(origLength1));
        assertFalse(chopEmpty.applyImpl(origLength2));
    }

    @Test
    public void test_methodMultiChain() {
        CALLS_TO_STRING_THING.chop().chop().empty();
        FunctorBase<StringThing,Boolean> chopEmpty = makeFunctorForTest();
        StringThing origLength2 = new StringThing("12");
        StringThing origLength3 = new StringThing("123");

        assertTrue(chopEmpty.applyImpl(origLength2));
        assertFalse(chopEmpty.applyImpl(origLength3));
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
