package org.funcito;

/**
 * Copyright 2013 Project Funcito Contributors
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.collect.Lists;
import org.junit.Test;
import rx.util.functions.Func1;

import java.util.List;

import static org.funcito.FuncitoRxJava.*;
import static org.junit.Assert.*;

public class FuncitoRxJavaFunc1_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private static class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
    }

    @Test
    public void testFunc1For_AssignToFunc1WithMatchingTypes() {
        Func1<StringThing, Integer> superTypeRet = func1For(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.call(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunc1For_AssignToFunc1WithSourceSuperType() {
        Func1<Object, Integer> superTypeRet = func1For(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.call(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunc1For_AssignToFuncWithTargetSuperType() {
        Func1<StringThing, ? extends Number> superType = func1For(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.call(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunc1For_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return 123; }
        }
        Func1<IntegerWrapperRet, Integer> wrapperIntFunc = func1For(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.call(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunc1For_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        Func1<PrimitiveIntRet, Integer> primIntFunc = func1For(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.call(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunc1For_MethodHasNonPrimitiveArrayRetType() {
        class NonPrimArrayRet {
            public Object[] getVal() { return new Object[] {"ABC", Integer.MAX_VALUE}; }
        }
        Func1<NonPrimArrayRet, Object[]> wrapperIntFunc = func1For(callsTo(NonPrimArrayRet.class).getVal());
        assertEquals(2, wrapperIntFunc.call(new NonPrimArrayRet()).length);
    }

    @Test
    public void testFunc1For_MethodHasPrimitiveArrayRetType() {
        class PrimArrayRet {
            public int[] getVal() { return new int[] {1,2,3}; }
        }
        Func1<PrimArrayRet, int[]> wrapperIntFunc = func1For(callsTo(PrimArrayRet.class).getVal());
        assertEquals(3, wrapperIntFunc.call(new PrimArrayRet()).length);
    }

    @Test
    public void testFunc1For_ValidateDetectsMismatchedGenericTypes() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        Func1<Generic<String>, Integer> stringFunc = func1For(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        stringFunc.call(integerGeneric);
    }

    @Test
    public void testFunc1For_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        Func1<Generic<? extends Object>, Integer> stringFunc = func1For(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.call(integerGeneric).intValue());
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        Func1<StringThing,String> pluralFunc = func1For(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.call(dog)));
        assertEquals("dog", pluralFunc.call(dog));
    }

    @Test
    public void testFunc1For_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        Func1<List<String>,String> getElem0Func = func1For(callsToList.get(0));
        Func1<List<String>,String> getElem2Func = func1For(callsToList.get(2));
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.call(list));
        assertEquals("Two", getElem2Func.call(list));
    }
}

