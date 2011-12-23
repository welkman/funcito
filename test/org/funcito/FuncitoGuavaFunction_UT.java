package org.funcito;

import com.google.common.base.Function;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.funcito.FuncitoGuava.*;

public class FuncitoGuavaFunction_UT {

    private static class StringThing {
        protected String myString;

        public StringThing(String myString) {
            this.myString = myString;
        }

        public int size() {
            return myString.length();
        }
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() {
        Function<StringThing, Integer> superTypeRet = functionFor(callsTo(StringThing.class).size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithSourceSuperType() {
        Function<Object, Integer> superTypeRet = functionFor(callsTo(StringThing.class).size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFuncWithTargetSuperType() {
        Function<StringThing, ? extends Number> superType = functionFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() {
                return 123;
            }
        }
        Function<IntegerWrapperRet, Integer> wrapperIntFunc = functionFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.apply(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() {
                return 123;
            }
        }
        Function<PrimitiveIntRet, Integer> primIntFunc = functionFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.apply(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctionFor_ValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        Function<Generic<String>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.apply(integerGeneric);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        Function<Generic<? extends Object>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.apply(integerGeneric).intValue());
    }
}

