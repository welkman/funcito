package org.funcito;

import com.google.common.base.Predicate;
import org.junit.Test;

import static org.funcito.Funcito.callsTo;
import static org.funcito.Funcito.predicateFor;
import static org.junit.Assert.assertTrue;

public class FuncitoGuavaPredicate_UT {

    private static class BooleanThing {
        private Boolean myVal;

        public BooleanThing(Boolean myVal) { this.myVal = myVal; }

        public Boolean getVal() { return myVal; }
    }

    @Test
    public void testPredicateFor_AssignToPredicateWithSourceSuperType() {
        Predicate<Object> superTypeRet = predicateFor(callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.apply(new BooleanThing(true)));
    }

    @Test
    public void testPredicateFor_MethodHasBooleanWrapperRetType() {
        class BooleanWrapperRet {
            public Boolean getBoolWrap() { return Boolean.TRUE; }
        }
        Predicate<BooleanWrapperRet> wrapBoolPred = predicateFor(callsTo(BooleanWrapperRet.class).getBoolWrap());
        assertTrue(wrapBoolPred.apply(new BooleanWrapperRet()));
    }

    @Test
    public void testPredicateFor_MethodHasPrimitiveBooleanRetType() {
        class PrimitiveBoolRet {
            public boolean getPrimBool() { return true; }
        }
        Predicate<PrimitiveBoolRet> primBoolPred = predicateFor(callsTo(PrimitiveBoolRet.class).getPrimBool());
        assertTrue(primBoolPred.apply(new PrimitiveBoolRet()));
    }

    @Test
    public void testPredicateFor_ValidateDetectsMismatchedGenericTypes() {
        Predicate<PrimitiveBoolRetGeneric<String>> stringPred = predicateFor(callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringPred.apply(integerGeneric);
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        Predicate<PrimitiveBoolRetGeneric<?>> stringPred = predicateFor(callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        assertTrue(stringPred.apply(integerGeneric));
    }

    // TODO: I have not defined a policy for the below yet, currently throws NPE
//    @Test
//    public void testApply_ReturnNullBooleanWrapper() {
//        Predicate<BooleanThing> pred = predicateFor(callsTo(BooleanThing.class).getVal());
//        BooleanThing nullThing = new BooleanThing(null);
//        pred.apply(nullThing);
//    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

