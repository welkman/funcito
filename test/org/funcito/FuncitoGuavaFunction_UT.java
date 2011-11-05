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


    static StringThing cglibStringThingStub = stub(StringThing.class);
    static Function<StringThing, Integer> cglibbyX = functionFor(cglibStringThingStub.size());
    // Below compiles but doesn't behave as expected (i.e., unlike FunkyJFunctional)
    // Currently it doesn't run at all, due to null return value somewhere?
//    static Function<StringThing, Integer> cglibby2 = functionFor(cglibStringThingStub.size() * 2);

    // alternative shortened 1-line notation
    static Function<StringThing, Integer> cglibby = functionFor(callsTo(StringThing.class).size());
    // alternate possible API???: makeFunc(forCallTo(...)  makeFunc(onCalls(...)  makeFuncFrom(stubbedCall

    public static class StringThing2 extends StringThing {
        // include a no-arg ctor
        public StringThing2() {super(""); myString = "123456";}
    }

    @Test
    public void testLibby_validateImproperStubCallsOutsideOfWrap() {
        cglibStringThingStub.size(); // should detect calling stubbedCallsTo outside of a wrap call
        try {
            Function<StringThing,Integer> attempt = Funcito.functionFor(cglibStringThingStub.size());
            fail("Should not have succeeded");
        } catch (Exception e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }

    class OtherThing { // does NOT extend BooleanThing or implement common.common interface, but they look identical
        private String myString;
        public OtherThing(String myString) { this.myString = myString; }
        public Integer size() { return myString.length(); }
    }

    @Test
    public void testLibby_detectMismatchOfSourceType() {
        OtherThing otherThingStub = Funcito.stub(OtherThing.class);
        try {
            Function<StringThing, Integer> func = Funcito.functionFor(otherThingStub.size());
            fail("Should not have allowed wrapping an OtherThing method as a StringThing function");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testLibby_assignToFuncWithSourceSuperType() {
        Function<StringThing, Integer> superType;
        superType = functionFor(callsTo(StringThing2.class).size());
    }

    @Test
    public void testLibby_assignToFuncWithTargetSuperType() {
        Function<StringThing,? extends Number> superType = functionFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6,n);
    }

}
