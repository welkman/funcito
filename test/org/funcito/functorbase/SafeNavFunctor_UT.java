package org.funcito.functorbase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PLEASE NOTICE: that this class extends BasicFunctor_UT, and inherits all tests from it
 */
public class SafeNavFunctor_UT extends BasicFunctor_UT {

    // This is what differentiates execution of inherited tests from parent test class BasicFunctor_UT
    @Override
    protected <T, V> BasicFunctor<T, V> makeFunctorForTest() {
        return new SafeNavFunctor<T, V>(getState(), null);
    }

    private A CALLS_TO_A = delegate.callsTo(A.class);

    private A a = new A();
    private B b = new B();
    private C c = new C();

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
    public void testNoChain() {
        CALLS_TO_A.getB(); // prime the pump
        SafeNavFunctor<A,B> func = new SafeNavFunctor<A,B>(getState(), b);

        assertSame(b, func.applyImpl(a));
    }

    @Test
    public void testNoChainNullDefault() {
        assertNull(a.getB());

        CALLS_TO_A.getB();
        SafeNavFunctor<A,B> func = new SafeNavFunctor<A,B>(getState(), null);

        assertNull(func.applyImpl(a));
    }

    @Test
    public void testChainedWithInitialNull() {
        CALLS_TO_A.getB().getC();
        SafeNavFunctor<A,C> func = new SafeNavFunctor<A,C>(getState(), c);

        assertSame(c, func.applyImpl(a));
    }

    @Test
    public void testChainedWithNonInitialNull() {
        CALLS_TO_A.getB().getC();
        C differentC = new C();
        SafeNavFunctor<A,C> func = new SafeNavFunctor<A,C>(getState(), differentC);

        a.setB(b);
        assertSame(differentC, func.applyImpl(a));
        assertNotSame(c, func.applyImpl(a));
    }

    @Test
    public void testChainedNoNulls() {
        C differentC = new C();
        a.setB(b);
        b.setC(c);

        CALLS_TO_A.getB().getC();
        SafeNavFunctor<A,C> func = new SafeNavFunctor<A,C>(getState(), differentC);

        assertSame(c, func.applyImpl(a));
        assertNotSame(differentC, func.applyImpl(a));
    }
}
