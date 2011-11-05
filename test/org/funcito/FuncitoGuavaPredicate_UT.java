package org.funcito;

import com.google.common.base.Predicate;
import info.piwai.funkyjfunctional.guava.Pred;
import org.junit.BeforeClass;
import org.junit.Test;

import static info.piwai.funkyjfunctional.guava.FunkyGuava.withPred;
import static org.funcito.Funcito.predicateFor;
import static org.funcito.Funcito.stub;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FuncitoGuavaPredicate_UT {

    private static class BooleanThing {
        private Boolean myVal;
        public BooleanThing(Boolean myVal) { this.myVal = myVal; }
        public Boolean getVal() { return myVal; }
    }


    BooleanThing cglibBooleanThingStub = stub(BooleanThing.class);
    Predicate<BooleanThing> cglibby = predicateFor(cglibBooleanThingStub.getVal());
//    Predicate<BooleanThing> cglibby2 = predicateFor(cglibBooleanThingStub.getVal() * 2);


    @Test
    public void testLibby_validateImproperStubCallsOutsideOfWrap() {
        cglibBooleanThingStub.getVal(); // should detect calling stubbedCallsTo outside of a wrap call
        try {
            Predicate<BooleanThing> attempt = Funcito.predicateFor(cglibBooleanThingStub.getVal());
            fail("Should not have succeeded");
        } catch (Exception e) {
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
    public void testLibby_mismatchOfSourceType() {
        OtherThing otherThingStub = Funcito.stub(OtherThing.class);
        try {
            Predicate<BooleanThing> func = Funcito.predicateFor(otherThingStub.getVal());
            fail("Should not have allowed this");
        } catch (Exception e) {
            // Happy Path
        }
    }

}
