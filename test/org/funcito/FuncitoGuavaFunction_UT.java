package org.funcito;

import com.google.common.base.Function;
import org.junit.Test;

import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

public class FuncitoGuavaFunction_UT {

    private static class StringThing {
        protected String myString;
        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
    }


    // Below compiles but doesn't behave as expected (i.e., unlike FunkyJFunctional)
    // Currently it doesn't run at all, due to null return value somewhere?
//    static Function<StringThing, Integer> cglibby_x_2 = functionFor(callsTo.size() * 2);

    @Test
    public void testValidateImproperCallOnStubOutsideOf_functionFor() {
        StringThing stringThingStub = stub(StringThing.class);
        stringThingStub.size(); // It is not caught here
        try {
            // but when you try to use it properly later, multiple invocations have been pushed on the stack
            Function<StringThing,Integer> attempt = functionFor(stringThingStub.size());
            fail("Should not have succeeded");
        } catch (FuncitoException e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }


    @Test
    public void testDetectMismatchOfSourceType() {
        class OtherThing { // does NOT extend StringThing nor implement a common interface, but their methods ("size()") look identical
            public Integer size() { return 123; }
        }
        OtherThing otherThingStub = Funcito.stub(OtherThing.class);
        try {
            // cannot catch mismatch at compile time or in building the Function
            Function<StringThing, Integer> func = functionFor(otherThingStub.size());
            // but does catch it at runtime when you try to apply it
            func.apply(new StringThing("ABCD"));
        } catch (FuncitoException e) {
            // Happy Path
        }
    }

    @Test
    public void testAssignToFunctionWithSourceSuperType() {
        Function<Object, Integer>  superTypeRet = functionFor(callsTo(StringThing.class).size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testAssignToFuncWithTargetSuperType() {
        Function<StringThing,? extends Number> superType = functionFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6,n);
    }

    @Test
    public void testMethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return Integer.valueOf(123); }
        }
        Function<IntegerWrapperRet,Integer> wrapperIntFunc = functionFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.apply(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testMethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        Function<PrimitiveIntRet,Integer> primIntFunc = functionFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.apply(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testWorksWithPrivateNoArgCtor() {
        Function<HasPrivateCtor0ForGF,Integer> func = functionFor(callsTo(HasPrivateCtor0ForGF.class).getVal());
        assertEquals(123, func.apply(HasPrivateCtor0ForGF.instance).intValue());
    }

    @Test
    public void testSourceTypeHasPrivateCtorWithArgs() {
        Function<HasPrivateCtor1ForGF,Integer> func = functionFor(callsTo(HasPrivateCtor1ForGF.class).getVal());
        assertEquals(123, func.apply(HasPrivateCtor1ForGF.instance).intValue());
    }

    @Test
    public void testValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() {return 123;}
        }
        Function<Generic<String>,Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.apply(integerGeneric);
    }

    @Test
    public void testAllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() {return 123;}
        }
        Function<Generic<? extends Object>,Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.apply(integerGeneric).intValue());
    }
}

class HasPrivateCtor0ForGF {
    static HasPrivateCtor0ForGF instance = new HasPrivateCtor0ForGF();
    public Integer getVal() { return 123; }
}

class HasPrivateCtor1ForGF {
    static HasPrivateCtor1ForGF instance = new HasPrivateCtor1ForGF(123);
    private Integer value;
    private HasPrivateCtor1ForGF(Integer value) { this.value = value; }
    public Integer getVal() { return value; }
}
