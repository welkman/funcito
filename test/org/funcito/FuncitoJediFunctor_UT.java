package org.funcito;

import jedi.functional.Functor;
import org.junit.Test;

import static org.funcito.Funcito.Jedi.*;
import static org.junit.Assert.assertEquals;

public class FuncitoJediFunctor_UT {

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
    public void testFunctorFor_AssignToFunctorWithMatchingTypes() {
        Functor<StringThing, Integer> superTypeRet = functorFor(callsTo(StringThing.class).size());
        assertEquals(3, superTypeRet.execute(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctorFor_AssignToFunctorWithSourceSuperType() {
        Functor<Object, Integer> superTypeRet = functorFor(callsTo(StringThing.class).size());
        assertEquals(3, superTypeRet.execute(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctorFor_AssignToFuncWithTargetSuperType() {
        Functor<StringThing, ? extends Number> superType = functorFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.execute(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctorFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() {
                return 123;
            }
        }
        Functor<IntegerWrapperRet, Integer> wrapperIntFunc = functorFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.execute(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctorFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() {
                return 123;
            }
        }
        Functor<PrimitiveIntRet, Integer> primIntFunc = functorFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.execute(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctorFor_ValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        Functor<Generic<String>, Integer> stringFunc = functorFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.apply(integerGeneric);
    }

    @Test
    public void testFunctorFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        Functor<Generic<? extends Object>, Integer> stringFunc = functorFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.execute(integerGeneric).intValue());
    }
}

