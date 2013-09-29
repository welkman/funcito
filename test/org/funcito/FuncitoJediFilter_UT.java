package org.funcito;

import jedi.functional.Filter;
import org.funcito.mode.Mode;
import org.funcito.mode.Modes;
import org.funcito.mode.UntypedMode;
import org.junit.Test;

import static org.funcito.FuncitoJedi.*;
import static org.junit.Assert.*;

public class FuncitoJediFilter_UT {

    public static final BooleanThing CALL_TO_BOOL_THING = callsTo(BooleanThing.class);

    private static class BooleanThing {
        private Boolean myVal;

        public BooleanThing(Boolean myVal) { this.myVal = myVal; }

        public Boolean getVal() { return myVal; }
    }

    @Test
    public void testFilterFor__AssignToFilterWithMatchingSourceType() { // I.e. vanilla happy path
        Filter<BooleanThing> superTypeRet = filterFor(CALL_TO_BOOL_THING.getVal());
        assertTrue(superTypeRet.execute(new BooleanThing(true)));
    }

    @Test
    public void testFilterFor_AssignToFilterWithSourceSuperType() {
        Filter<Object> superTypeRet = filterFor(callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.execute(new BooleanThing(true)));
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        Filter<PrimitiveBoolRetGeneric<?>> stringPred = filterFor(callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        assertTrue(stringPred.execute(integerGeneric));
    }

    @Test
    public void testFilterFor_ExpressionsWithOperatorsAreUnsupported() {
        Filter<BooleanThing> boolInstancePred = filterFor( CALL_TO_BOOL_THING.getVal() instanceof Boolean);

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse(boolInstancePred.execute(new BooleanThing(false))); // does not return true because operator not captured
    }

    @Test
    public void testPredicateFor_UntypedMode() {
        BooleanThing nullThing = new BooleanThing(true);

        UntypedMode mode = Modes.noOp();
        Filter<BooleanThing> pred = filterFor(CALL_TO_BOOL_THING.getVal(), mode);
        assertTrue(pred.execute(nullThing));
    }

    @Test
    public void testPredicateFor_Mode() {
        BooleanThing nullThing = new BooleanThing(null);

        Mode<BooleanThing,Boolean> mode = Modes.safeNav(true);
        Filter<BooleanThing> pred = filterFor(CALL_TO_BOOL_THING.getVal(), mode);
        assertTrue(pred.execute(nullThing));

        // do the same test for "false"
        mode = Modes.safeNav(false);
        pred = filterFor(CALL_TO_BOOL_THING.getVal(), mode);
        assertFalse(pred.execute(nullThing));
    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

