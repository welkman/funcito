package org.funcito;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.funcito.FuncitoGuava.*;
import static org.funcito.modifier.UntypedSafeNav.SAFE_NAV;
import static org.funcito.modifier.Modifiers.*;
import static org.junit.Assert.*;

public class FuncitoGuavaFunction_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString==null ? 0 : myString.length(); }
        public String toString() { return myString; }
        public StringThing plusABC() { return new StringThing(this.toString() + "ABC"); }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() { // I.e. vanilla happy path
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_RepeatedUsage() {
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
        assertEquals(6, superTypeRet.apply(new StringThing("ABCDEF")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFunctionWithSourceSuperType() {
        Function<Object, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
    }

    @Test
    public void testFunctionFor_AssignToFuncWithTargetSuperType() {
        Function<StringThing, ? extends Number> superType = functionFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6, n);
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveWrapperRetType() {
        class IntegerWrapperRet {
            public Integer getVal() { return 123; }
        }
        Function<IntegerWrapperRet, Integer> wrapperIntFunc = functionFor(callsTo(IntegerWrapperRet.class).getVal());
        assertEquals(123, wrapperIntFunc.apply(new IntegerWrapperRet()).intValue());
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveRetType() {
        class PrimitiveIntRet {
            public int getVal() { return 123; }
        }
        Function<PrimitiveIntRet, Integer> primIntFunc = functionFor(callsTo(PrimitiveIntRet.class).getVal());
        assertEquals(123, primIntFunc.apply(new PrimitiveIntRet()).intValue());
    }

    @Test
    public void testFunctionFor_MethodHasNonPrimitiveArrayRetType() {
        class NonPrimArrayRet {
            public Object[] getVal() { return new Object[] {"ABC", Integer.MAX_VALUE}; }
        }
        Function<NonPrimArrayRet, Object[]> wrapperIntFunc = functionFor(callsTo(NonPrimArrayRet.class).getVal());
        assertEquals(2, wrapperIntFunc.apply(new NonPrimArrayRet()).length);
    }

    @Test
    public void testFunctionFor_MethodHasPrimitiveArrayRetType() {
        class PrimArrayRet {
            public int[] getVal() { return new int[] {1,2,3}; }
        }
        Function<PrimArrayRet, int[]> wrapperIntFunc = functionFor(callsTo(PrimArrayRet.class).getVal());
        assertEquals(3, wrapperIntFunc.apply(new PrimArrayRet()).length);
    }

    @Test
    public void testFunctionFor_ValidateDetectsMismatchedGenericTypes() {
        Function<List<String>, Integer> stringListFunc = functionFor(callsTo(List.class).size());
        List<Integer> integerList = new ArrayList<Integer>();

        // The below can't actually be compiled, which proves the test passes: compile time mismatch detection
        // One of these days I hope to write the code to use JDK compiler to prove that the above compiles
        // but the below does not.
//        stringListFunc.apply(integerList);
    }

    @Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        class AnyOldClass{}
        Function<Generic<?>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<AnyOldClass> anyOldGeneric = new Generic<AnyOldClass>();

        assertEquals(123, stringFunc.apply(anyOldGeneric).intValue());
    }

    @Test
    public void testFunctionFor_ExpressionsWithOperatorsAreUnsupported() {
        Function<StringThing,String> pluralFunc = functionFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.apply(dog)));
        assertEquals("dog", pluralFunc.apply(dog));
    }

    @Test
    public void testFunctionFor_SingleArgBinding() {
        List<String> callsToList = callsTo(List.class);
        Function<List<String>,String> getElem0Func = functionFor(callsToList.get(0));
        Function<List<String>,String> getElem2Func = functionFor(callsToList.get(2));
        List<String> list = Lists.newArrayList("Zero", "One", "Two");

        assertEquals("Zero", getElem0Func.apply(list));
        assertEquals("Two", getElem2Func.apply(list));
    }

    @Test
    public void testFunctionFor_methodChain() {
        Function<StringThing,Integer> sizePlus3Func = functionFor(CALLS_TO_STRING_THING.plusABC().size());
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(6, (int) sizePlus3Func.apply(origLength3));
        assertEquals(9, (int) sizePlus3Func.apply(origLength6));
    }

    @Test
    public void testFunctionFor_methodMultiChain() {
        Function<StringThing,Integer> sizePlus3Plus3Func = functionFor(CALLS_TO_STRING_THING.plusABC().plusABC().size());
        StringThing origLength3 = new StringThing("123");
        StringThing origLength6 = new StringThing("123456");

        assertEquals(9, (int)sizePlus3Plus3Func.apply(origLength3));
        assertEquals(12, (int) sizePlus3Plus3Func.apply(origLength6));
    }

    @Test(expected = NullPointerException.class)
    public void testFunctionFor_MethodChainingAttemptWithUnproxyableInterimType() {
        // NOTE: this test is a test that proves and documents a limitation of Funcito: interim type String
        // is final, hence non-proxyable
        functionFor(CALLS_TO_STRING_THING.toString().length());
    }

    class A {
        private B b;
        public B getB() { return b;}
        public void setB(B b) { this.b = b; }
    }

    class B {
        private C c;
        public C getC() { return c;}
        public void setC(C c) { this.c = c; }
    }

    class C {}

    @Test
    public void testFunctionFor_SafeNav_NoChain() {
        A a = new A();
        B b = new B(); // not setting this value into A

        Function<A,B> func = functionFor(callsTo(A.class).getB(), safeNav(b));

        assertSame(b, func.apply(a));
    }

    @Test
    public void testFunctionFor_SafeNav_NoChainNullDefault() {
        A a = new A();
        assertNull(a.getB());

        // Note that in parameterized version, null must be cast for compile to work.
        Function<A,B> func = functionFor(callsTo(A.class).getB(), safeNav((B)null));
        Function<A,B> func2 = functionFor(callsTo(A.class).getB(), safeNav());
        Function<A,B> func3 = functionFor(callsTo(A.class).getB(), SAFE_NAV);

        assertNull(func.apply(a));
        assertNull(func2.apply(a));
        assertNull(func3.apply(a));
    }

    @Test
    public void testFunctionFor_SafeNav_ChainedWithInitialNull() {
        A a = new A();
        C c = new C();

        Function<A,C> func = functionFor(callsTo(A.class).getB().getC(), safeNav(c));

        assertSame(c, func.apply(a));
    }

    @Test
    public void testFunctionFor_SafeNav_ChainedNoNulls() {
        A a = new A();
        B b = new B();
        C c = new C();
        C differentC = new C();
        a.setB(b);
        b.setC(c);

        Function<A,C> func = functionFor(callsTo(A.class).getB().getC(), safeNav(differentC));

        assertSame(c, func.apply(a));
        assertNotSame(differentC, func.apply(a));
    }
}

