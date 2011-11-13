package org.funcito;

import com.google.common.base.Predicate;
import org.junit.Test;

import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

public class FuncitoGuavaPredicate_UT {

    private static class BooleanThing {
        private Boolean myVal;
        public BooleanThing(Boolean myVal) { this.myVal = myVal; }
        public Boolean getVal() { return myVal; }
    }

    @Test
    public void testPredicateFor_ValidateImproperCallToStubOutsideOf_predicateFor() {
        BooleanThing booleanThingStub = stub(BooleanThing.class);
        booleanThingStub.getVal(); // It is not caught here

        try {
            // but when you try to use it properly later, multiple invocations have been pushed on the stack
            Predicate<BooleanThing> attempt = predicateFor(booleanThingStub.getVal());
            fail("Should not have succeeded");
        } catch (FuncitoException e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }

    @Test
    public void testPredicateFor_AssignToPredicateWithSourceSuperType() {
        Predicate<Object>  superTypeRet = predicateFor(callsTo(BooleanThing.class).getVal());
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
        class Generic<T> {
            public boolean getVal() {return true;}
        }
        Predicate<Generic<String>> stringPred = predicateFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringPred.apply(integerGeneric);
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public boolean getVal() {return true;}
        }
        Predicate<Generic<? extends Object>> stringPred = predicateFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertTrue(stringPred.apply(integerGeneric));
    }

}

