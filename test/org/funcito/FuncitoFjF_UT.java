package org.funcito;

import fj.F;
import org.junit.Test;

import static org.funcito.Funcito.Fj.*;
import static org.junit.Assert.assertEquals;

public class FuncitoFjF_UT {

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
    public void testFunctionFor_AssignToFunctionWithSourceSuperType() {
        F<Object, Integer> superTypeRet = fFor(callsTo(StringThing.class).size());
        assertEquals(3, superTypeRet.f(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFuncWithTargetSuperType() {
        F<StringThing, ? extends Number> superType = fFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.f(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() {
                return 123;
            }
        }
        F<IntegerWrapperRet, Integer> wrapperIntFunc = fFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.f(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() {
                return 123;
            }
        }
        F<PrimitiveIntRet, Integer> primIntFunc = fFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.f(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctionFor_ValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        F<Generic<String>, Integer> stringFunc = fFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.f(integerGeneric);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        F<Generic<? extends Object>, Integer> stringFunc = fFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.f(integerGeneric).intValue());
    }
}

