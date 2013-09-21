package org.funcito.functorbase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class PrimitivePredicate_UT extends FunctorBaseTestBase {

    // This is what differentiates execution of inherited tests from parent test class FunctorBaseTestBase
    @Override
    protected <T, V> FunctorBase<T, V> makeFunctorForTest() {
        return (FunctorBase<T, V>) new PrimitivePredicate<T>(getState(), true);
    }

    private final BoolThing CALLS_TO_BOOL_THING = delegate.callsTo(BoolThing.class);

    private class BoolThing {
        Boolean val = null;
        BoolThing(Boolean val) { this.val = val; }
        Boolean returnVal() { return val; }
    }

    @Test
    public void testDefaultUsed() {
        CALLS_TO_BOOL_THING.returnVal(); // prime the pump
        PrimitivePredicate<BoolThing> predDefaultTrue = new PrimitivePredicate<BoolThing>(getState(), true);
        CALLS_TO_BOOL_THING.returnVal();
        PrimitivePredicate<BoolThing> predDefaultFalse = new PrimitivePredicate<BoolThing>(getState(), false);

        assertTrue(predDefaultTrue.applyImpl(new BoolThing(null)));
        assertFalse(predDefaultFalse.applyImpl(new BoolThing(null)));
    }

    @Test
    public void testDefaultNotUsed() {
        CALLS_TO_BOOL_THING.returnVal(); // prime the pump
        PrimitivePredicate<BoolThing> predDefaultTrue = new PrimitivePredicate<BoolThing>(getState(), true);
        CALLS_TO_BOOL_THING.returnVal();
        PrimitivePredicate<BoolThing> predDefaultFalse = new PrimitivePredicate<BoolThing>(getState(), false);

        assertFalse(predDefaultTrue.applyImpl(new BoolThing(false)));
        assertTrue(predDefaultFalse.applyImpl(new BoolThing(true)));
    }
}
