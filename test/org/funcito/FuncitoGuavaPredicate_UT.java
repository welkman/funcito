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


    BooleanThing booleanThingStub = stub(BooleanThing.class);
//    Predicate<BooleanThing> pred2 = predicateFor(booleanThingStub.getVal() * 2);


    @Test
    public void testValidateImproperStubCallsOutsideOfWrap() {
        booleanThingStub.getVal(); // should detect calling stubbedCallsTo outside of a wrap call
        try {
            Predicate<BooleanThing> attempt = predicateFor(booleanThingStub.getVal());
            fail("Should not have succeeded");
        } catch (FuncitoException e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }

    class OtherThing { // does NOT extend BooleanThing or implement common.common interface, but they look identical
        private Boolean value;
        public OtherThing(Boolean value) { this.value = value; }
        public Boolean getVal() { return value; }
    }

    @Test
    public void test_detectMismatchOfSourceType() {
        OtherThing otherThingStub = stub(OtherThing.class);
        try {
            Predicate<BooleanThing> pred = predicateFor(otherThingStub.getVal());
            fail("Should not have allowed this");
        } catch (FuncitoException e) {
            // Happy Path
        }
    }

}
