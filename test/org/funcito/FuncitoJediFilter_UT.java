package org.funcito;

import jedi.functional.Filter;
import org.junit.Test;

import static org.funcito.Funcito.Jedi.*;
import static org.junit.Assert.*;

public class FuncitoJediFilter_UT {

    private static class BooleanThing {
        private Boolean myVal;

        public BooleanThing(Boolean myVal) { this.myVal = myVal; }

        public Boolean getVal() { return myVal; }
    }

    @Test
    public void testFilterFor_AssignToFilterWithSourceSuperType() {
        Filter<Object> superTypeRet = filterFor(callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.execute(new BooleanThing(true)));
    }

    @Test
    public void testFilterFor_MethodHasBooleanWrapperRetType() {
        class BooleanWrapperRet {
            public Boolean getBoolWrap() { return Boolean.TRUE; }
        }
        Filter<BooleanWrapperRet> wrapBoolPred = filterFor(callsTo(BooleanWrapperRet.class).getBoolWrap());
        assertTrue(wrapBoolPred.execute(new BooleanWrapperRet()));
    }

    @Test
    public void testFilterFor_MethodHasPrimitiveBooleanRetType() {
        class PrimitiveBoolRet {
            public boolean getPrimBool() { return true; }
        }
        Filter<PrimitiveBoolRet> primBoolPred = filterFor(callsTo(PrimitiveBoolRet.class).getPrimBool());
        assertTrue(primBoolPred.execute(new PrimitiveBoolRet()));
    }

    @Test
    public void testFilterFor_ValidateDetectsMismatchedGenericTypes() {
        Filter<PrimitiveBoolRetGeneric<String>> stringPred = filterFor(callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringPred.execute(integerGeneric);
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        Filter<PrimitiveBoolRetGeneric<?>> stringPred = filterFor(callsTo(PrimitiveBoolRetGeneric.class).getVal());
        PrimitiveBoolRetGeneric<Integer> integerGeneric = new PrimitiveBoolRetGeneric<Integer>();

        assertTrue(stringPred.execute(integerGeneric));
    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

