package org.funcito;

import org.apache.commons.collections15.Predicate;
import org.funcito.internal.WrapperType;
import org.funcito.mode.Mode;
import org.funcito.mode.Modes;
import org.funcito.mode.UntypedMode;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.funcito.FuncitoCollectGen.*;
import static org.junit.Assert.*;

public class FuncitoCollectGenPredicate_UT {

    public static final BooleanThing CALL_TO_BOOL_THING = callsTo(BooleanThing.class);

    @Rule public ExpectedException thrown = ExpectedException.none();

    private static class BooleanThing {
        private Boolean myVal;

        public BooleanThing(Boolean myVal) { this.myVal = myVal; }

        public Boolean getVal() { return myVal; }
    }

    @After
    public void tearDown() {
        try {
            delegate().extractInvokableState(WrapperType.COLLECTGEN_PREDICATE);
        } catch (Throwable t) {}
    }

    @Test
    public void testPredicateFor__AssignToPredicateWithMatchingTypes() { // I.e. vanilla happy path
        Predicate<BooleanThing> superTypeRet = predicateFor(CALL_TO_BOOL_THING.getVal());
        assertTrue(superTypeRet.evaluate(new BooleanThing(true)));
    }

    @Test
    public void testPredicateFor_AssignToPredicateWithSourceSuperType() {
        Predicate<Object> superTypeRet = predicateFor(CALL_TO_BOOL_THING.getVal());
        assertTrue(superTypeRet.evaluate(new BooleanThing(true)));
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        Predicate<PrimitiveBoolRetGeneric<?>> stringPred = predicateFor(callsTo(PrimitiveBoolRetGeneric.class).getVal());
        class AnyOldClass{}
        PrimitiveBoolRetGeneric<AnyOldClass> anyOldGeneric = new PrimitiveBoolRetGeneric<AnyOldClass>();

        assertTrue(stringPred.evaluate(anyOldGeneric));
    }

    @Test
    public void testApply_ReturnNullBooleanWrapper() {
        Predicate<BooleanThing> pred = predicateFor(CALL_TO_BOOL_THING.getVal());
        BooleanThing nullThing = new BooleanThing(null);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Predicate returned a null Boolean");
        pred.evaluate(nullThing);
    }

    @Test
    public void testPredicateFor_SafeVersion() {
        BooleanThing nullThing = new BooleanThing(null);

        Predicate<BooleanThing> pred = predicateFor(CALL_TO_BOOL_THING.getVal(), true);
        assertTrue(pred.evaluate(nullThing));

        // do the same test for "false"
        pred = predicateFor(CALL_TO_BOOL_THING.getVal(), false);
        assertFalse(pred.evaluate(nullThing));
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        Predicate<BooleanThing> boolInstancePred = predicateFor( CALL_TO_BOOL_THING.getVal() instanceof Boolean);

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse( boolInstancePred.evaluate(new BooleanThing(false)) ); // does not return true because operator not captured
    }

    @Test
    public void testPredicateFor_UntypedMode() {
        BooleanThing nullThing = new BooleanThing(null);

        UntypedMode untypedMode = Modes.defaultBool(true);
        Predicate<BooleanThing> pred = predicateFor(CALL_TO_BOOL_THING.getVal(), untypedMode);
        assertTrue(pred.evaluate(nullThing));

        // do the same test for "false"
        untypedMode = Modes.defaultBool(false);
        pred = predicateFor(CALL_TO_BOOL_THING.getVal(), untypedMode);
        assertFalse(pred.evaluate(nullThing));
    }

    @Test
    public void testPredicateFor_Mode() {
        BooleanThing nullThing = new BooleanThing(null);

        Mode<Object,Boolean> mode = Modes.safeNav(true);
        Predicate<BooleanThing> pred = predicateFor(CALL_TO_BOOL_THING.getVal(), mode);
        assertTrue(pred.evaluate(nullThing));

        // do the same test for "false"
        mode = Modes.safeNav(false);
        pred = predicateFor(CALL_TO_BOOL_THING.getVal(), mode);
        assertFalse(pred.evaluate(nullThing));
    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

