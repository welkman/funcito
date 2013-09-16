package org.funcito;

import com.google.common.base.Function;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import static org.funcito.FuncitoGuava.*;
import static org.funcito.mode.UntypedSafeNav.SAFE_NAV;
import static org.funcito.mode.Modes.*;
import static org.junit.Assert.*;

public class FuncitoGuavaFunction_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString==null ? 0 : myString.length(); }
        public String toString() { return myString; }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    // TODO: rename as happy path in all API UTs
    @Test
    public void testFunctionFor_AssignToFunctionWithMatchingTypes() { // I.e. vanilla happy path
        Function<StringThing, Integer> superTypeRet = functionFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.apply(new StringThing("ABC")).intValue());
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

