package org.funcito.functorbase;

import org.funcito.FuncitoException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class TailDefaultFunctor_UT extends FunctorBaseTestBase {

    @Rule public ExpectedException expected = ExpectedException.none();

    // This is what differentiates execution of inherited tests from parent test class FunctorBaseTestBase
    @Override
    protected <T, V> FunctorBase<T, V> makeFunctorForTest() {
        return new TailDefaultFunctor<T,V>(getState(), null);
    }

    private final BoolThing CALLS_TO_BOOL_THING = delegate.callsTo(BoolThing.class);

    private class BoolThing {
        private Boolean val = null;
        private BoolThing child = null;
        BoolThing(Boolean val) { this.val = val; }
        Boolean returnVal() { return val; }
        BoolThing child() { return child; }
    }

    @Test
    public void testPrimitivePredicate_DefaultUsed() {
        CALLS_TO_BOOL_THING.returnVal(); // prime the pump
        TailDefaultFunctor<BoolThing,Boolean> predDefaultTrue = new TailDefaultFunctor<BoolThing,Boolean>(getState(), true);
        CALLS_TO_BOOL_THING.returnVal();
        TailDefaultFunctor<BoolThing,Boolean> predDefaultFalse = new TailDefaultFunctor<BoolThing,Boolean>(getState(), false);

        assertTrue(predDefaultTrue.applyImpl(new BoolThing(null)));
        assertFalse(predDefaultFalse.applyImpl(new BoolThing(null)));
    }

    @Test
    public void testPrimitivePredicate_DefaultNotUsed() {
        CALLS_TO_BOOL_THING.returnVal(); // prime the pump
        TailDefaultFunctor<BoolThing,Boolean> predDefaultTrue = new TailDefaultFunctor<BoolThing,Boolean>(getState(), true);
        CALLS_TO_BOOL_THING.returnVal();
        TailDefaultFunctor<BoolThing,Boolean> predDefaultFalse = new TailDefaultFunctor<BoolThing,Boolean>(getState(), false);

        assertFalse(predDefaultTrue.applyImpl(new BoolThing(false)));
        assertTrue(predDefaultFalse.applyImpl(new BoolThing(true)));
    }
    
    @Test
    public void testPrimitivePredicate_DoesNotDoSafeNavigation() {
        CALLS_TO_BOOL_THING.child().returnVal();
        TailDefaultFunctor<BoolThing,Boolean> predicate = new TailDefaultFunctor<BoolThing,Boolean>(getState(), true);

        expected.expect(FuncitoException.class);
        predicate.applyImpl(new BoolThing(null));
    }
}
