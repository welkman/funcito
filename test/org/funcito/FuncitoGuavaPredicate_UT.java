package org.funcito;

import com.google.common.base.Predicate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

public class FuncitoGuavaPredicate_UT {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private static class BooleanThing {
        private Boolean myVal;

        public BooleanThing(Boolean myVal) { this.myVal = myVal; }

        public Boolean getVal() { return myVal; }
    }

    @Test
    public void testPredicateFor_AssignToPredicateWithSourceSuperType() {
        Predicate<Object> superTypeRet = Guava.predicateFor(Guava.callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.apply(new BooleanThing(true)));
    }

    @Test
    public void testPredicateFor_MethodHasBooleanWrapperRetType() {
        class BooleanWrapperRet {
            public Boolean getBoolWrap() { return Boolean.TRUE; }
        }
        Predicate<BooleanWrapperRet> wrapBoolPred = Guava.predicateFor(Guava.callsTo(BooleanWrapperRet.class).getBoolWrap());
        assertTrue(wrapBoolPred.apply(new BooleanWrapperRet()));
    }

    @Test
    public void testPredicateFor_MethodHasPrimitiveBooleanRetType() {
        class PrimitiveBoolRet {
            public boolean getPrimBool() { return true; }
        }
        Predicate<PrimitiveBoolRet> primBoolPred = Guava.predicateFor(Guava.callsTo(PrimitiveBoolRet.class).getPrimBool());
        assertTrue(primBoolPred.apply(new PrimitiveBoolRet()));
    }

    @Test
    public void testPredicateFor_ValidateDetectsMismatchedGenericTypes() {
        Predicate<PrimitiveBoolRetGeneric<String>> stringPred = Guava.predicateFor(Guava.callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringPred.apply(integerGeneric);
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        Predicate<PrimitiveBoolRetGeneric<?>> stringPred = Guava.predicateFor(Guava.callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        assertTrue(stringPred.apply(integerGeneric));
    }

    @Test
    public void testApply_ReturnNullBooleanWrapper() {
        Predicate<BooleanThing> pred = Guava.predicateFor(Guava.callsTo(BooleanThing.class).getVal());
        BooleanThing nullThing = new BooleanThing(null);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Predicate had a null Boolean");
        pred.apply(nullThing);
    }

    @Test
    public void testPredicateFor_SafeVersion() {
        BooleanThing nullThing = new BooleanThing(null);

        Predicate<BooleanThing> pred = Guava.predicateFor(Guava.callsTo(BooleanThing.class).getVal(), true);
        assertTrue(pred.apply(nullThing));

        // do the same test for "false"
        pred = Guava.predicateFor(Guava.callsTo(BooleanThing.class).getVal(), false);
        assertFalse(pred.apply(nullThing));
    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

