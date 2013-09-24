package org.funcito.functorbase;

import org.funcito.FuncitoException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class PrimitivePredicate_UT extends FunctorBaseTestBase {

    @Rule public ExpectedException expected = ExpectedException.none();

    // This is what differentiates execution of inherited tests from parent test class FunctorBaseTestBase
    @Override
    protected <T, V> FunctorBase<T, V> makeFunctorForTest() {
        return (FunctorBase<T, V>) new PrimitivePredicate<T>(getState(), true);
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
        PrimitivePredicate<BoolThing> predDefaultTrue = new PrimitivePredicate<BoolThing>(getState(), true);
        CALLS_TO_BOOL_THING.returnVal();
        PrimitivePredicate<BoolThing> predDefaultFalse = new PrimitivePredicate<BoolThing>(getState(), false);

        assertTrue(predDefaultTrue.applyImpl(new BoolThing(null)));
        assertFalse(predDefaultFalse.applyImpl(new BoolThing(null)));
    }

    @Test
    public void testPrimitivePredicate_DefaultNotUsed() {
        CALLS_TO_BOOL_THING.returnVal(); // prime the pump
        PrimitivePredicate<BoolThing> predDefaultTrue = new PrimitivePredicate<BoolThing>(getState(), true);
        CALLS_TO_BOOL_THING.returnVal();
        PrimitivePredicate<BoolThing> predDefaultFalse = new PrimitivePredicate<BoolThing>(getState(), false);

        assertFalse(predDefaultTrue.applyImpl(new BoolThing(false)));
        assertTrue(predDefaultFalse.applyImpl(new BoolThing(true)));
    }
    
    @Test
    public void testPrimitivePredicate_DoesNotDoSafeNavigation() {
        CALLS_TO_BOOL_THING.child().returnVal();
        PrimitivePredicate<BoolThing> predicate = new PrimitivePredicate<BoolThing>(getState(), true);

        expected.expect(FuncitoException.class);
        predicate.applyImpl(new BoolThing(null));
    }
}
