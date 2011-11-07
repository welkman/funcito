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
    public void testValidateImproperCallToStubOutsideOf_predicateFor() {
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
    public void testDetectMismatchOfSourceType() {
        class MismatchClass { // does NOT extend BooleanThing nor implement a common interface, but their methods ("getVal()") look identical
            private Boolean value = true;
            public Boolean getVal() { return value; }
        }
        MismatchClass otherThingStub = stub(MismatchClass.class);
        try {
            // cannot catch mismatch at compile time or in building the Predicate
            Predicate<BooleanThing> pred = predicateFor(otherThingStub.getVal());
            // but does catch it at runtime when you try to apply it
            pred.apply(new BooleanThing(true));
        } catch (FuncitoException e) {
            // Happy Path
            assertTrue(e.getMessage().contains("error trying to invoke"));
        }
    }

    @Test
    public void testAssignToPredicateWithSourceSuperType() {
        Predicate<Object>  superTypeRet = predicateFor(callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.apply(new BooleanThing(true)));
    }

    @Test
    public void testMethodHasBooleanWrapperRetType() {
        class BooleanWrapperRet {
            public Boolean getBoolWrap() { return Boolean.TRUE; }
        }
        Predicate<BooleanWrapperRet> wrapBoolPred = predicateFor(callsTo(BooleanWrapperRet.class).getBoolWrap());
        assertTrue(wrapBoolPred.apply(new BooleanWrapperRet()));
    }

    @Test
    public void testMethodHasPrimitiveBooleanRetType() {
        class PrimitiveBoolRet {
            public boolean getPrimBool() { return true; }
        }
        Predicate<PrimitiveBoolRet> primBoolPred = predicateFor(callsTo(PrimitiveBoolRet.class).getPrimBool());
        assertTrue(primBoolPred.apply(new PrimitiveBoolRet()));
    }

    @Test
    public void testSourceTypeHasPrivateNoArgCtor() {
        Predicate<HasPrivateCtor0ForGP> pred = predicateFor(callsTo(HasPrivateCtor0ForGP.class).getVal());
        assertTrue(pred.apply(HasPrivateCtor0ForGP.instance));
    }

    @Test
    public void testSourceTypeHasPrivateCtorWithArgs() {
        Predicate<HasPrivateCtor1ForGP> pred = predicateFor(callsTo(HasPrivateCtor1ForGP.class).getVal());
        assertTrue(pred.apply(HasPrivateCtor1ForGP.instance));
    }

    @Test
    public void testValidateDetectsMismatchedGenericTypes() {
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

class HasPrivateCtor0ForGP {
    static HasPrivateCtor0ForGP instance = new HasPrivateCtor0ForGP();
    public boolean getVal() { return true; }
}

class HasPrivateCtor1ForGP {
    static HasPrivateCtor1ForGP instance = new HasPrivateCtor1ForGP(true);
    private boolean value;
    private HasPrivateCtor1ForGP(boolean value) { this.value = value; }
    public boolean getVal() { return value; }
}
