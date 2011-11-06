package org.funcito;

import com.google.common.base.Function;
import org.junit.Test;

import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

public class FuncitoGuavaFunction_UT {

    private static class StringThing {
        protected String myString;
        public StringThing(String myString) { this.myString = myString; }
        public Integer size() { return myString.length(); }
        // NOTE: I could have returned the primitive itself, but that means that all Function solutions
        // would have the overhead of autoboxing, making the comparison to non-functional unfair
    }


    static StringThing stringThingStub = stub(StringThing.class);
    // Below compiles but doesn't behave as expected (i.e., unlike FunkyJFunctional)
    // Currently it doesn't run at all, due to null return value somewhere?
//    static Function<StringThing, Integer> cglibby_x_2 = functionFor(callsTo.size() * 2);

    public static class StringThing2 extends StringThing {
        // include a no-arg ctor
        public StringThing2() {super(""); myString = "123456";}
    }

    @Test
    public void testValidateImproperStubCallsOutsideOfWrap() {
        stringThingStub.size(); // should detect calling stringThingStub outside of a wrap call
        try {
            Function<StringThing,Integer> attempt = functionFor(stringThingStub.size());
            fail("Should not have succeeded");
        } catch (FuncitoException e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }

    private class OtherThing { // does NOT extend BooleanThing or implement common.common interface, but they look identical
        private String myString;
        public OtherThing(String myString) { this.myString = myString; }
        public Integer size() { return myString.length(); }
    }

    @Test
    public void testDetectMismatchOfSourceType() {
        OtherThing otherThingStub = Funcito.stub(OtherThing.class);
        try {
            Function<StringThing, Integer> func = functionFor(otherThingStub.size());
            fail("Should not have allowed wrapping an OtherThing method as a StringThing function");
        } catch (FuncitoException e) {
            // Happy Path
        }
    }

    @Test
    public void testAssignToFuncWithSourceSuperType() {
        Function<StringThing, Integer> superType;
        superType = functionFor(callsTo(StringThing2.class).size());
    }

    @Test
    public void testAssignToFuncWithTargetSuperType() {
        Function<StringThing,? extends Number> superType = functionFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6,n);
    }

}
