package org.funcito;

import jedi.functional.Filter;
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
        assertFalse( boolInstancePred.execute(new BooleanThing(false)) ); // does not return true because operator not captured
    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

