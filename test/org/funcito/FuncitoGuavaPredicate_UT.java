package org.funcito;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.funcito.FuncitoGuava.*;
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
    public void testPredicateFor__AssignToPredicateWithMatchingTypes() { // I.e. vanilla happy path
        Predicate<BooleanThing> superTypeRet = predicateFor(callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.apply(new BooleanThing(true)));
    }

    @Test
    public void testPredicateFor_RepeatedUsage() {
        Predicate<BooleanThing> superTypeRet = predicateFor(callsTo(BooleanThing.class).getVal());
        assertTrue(superTypeRet.apply(new BooleanThing(true)));
        assertFalse(superTypeRet.apply(new BooleanThing(false)));
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
        class AnyOldClass{}
        PrimitiveBoolRetGeneric<AnyOldClass> anyOldGeneric = new PrimitiveBoolRetGeneric<AnyOldClass>();

        assertTrue(stringPred.apply(anyOldGeneric));
    }

    @Test
    public void testApply_ReturnNullBooleanWrapper() {
        Predicate<BooleanThing> pred = predicateFor(callsTo(BooleanThing.class).getVal());
        BooleanThing nullThing = new BooleanThing(null);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Predicate had a null Boolean");
        pred.apply(nullThing);
    }

    @Test
    public void testPredicateFor_SafeVersion() {
        BooleanThing nullThing = new BooleanThing(null);

        Predicate<BooleanThing> pred = predicateFor(callsTo(BooleanThing.class).getVal(), true);
        assertTrue(pred.apply(nullThing));

        // do the same test for "false"
        pred = predicateFor(callsTo(BooleanThing.class).getVal(), false);
        assertFalse(pred.apply(nullThing));
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        Predicate<BooleanThing> boolInstancePred = predicateFor( callsTo(BooleanThing.class).getVal() instanceof Boolean);
        BooleanThing falseThing = new BooleanThing(false);

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse( boolInstancePred.apply(falseThing) ); // does not return true because operator not captured
    }

    @Ignore // Having problems with type erasure on Generics, now that stub does not return null for Objects
    @Test
    public void testPredicateFor_SingleArgBinding() {
        List<Boolean> CALLS_TO_LIST = callsTo(List.class);
        Predicate<List> item0Pred = predicateFor(CALLS_TO_LIST.get(0));
        Predicate<List> item1Pred = predicateFor(CALLS_TO_LIST.get(1));

        List<Boolean> list = Lists.newArrayList(true, false);
        assertTrue(item0Pred.apply(list));
        assertFalse(item1Pred.apply(list));
    }

    class PrimitiveBoolRetGeneric<T> {
        public boolean getVal() { return true; }
    }
}

