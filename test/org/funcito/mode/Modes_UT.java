package org.funcito.mode;

import org.funcito.FuncitoException;
import org.funcito.functorbase.BasicFunctor;
import org.funcito.functorbase.FunctorBase;
import org.funcito.functorbase.SafeNavFunctor;
import org.funcito.functorbase.TailDefaultFunctor;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class Modes_UT {

    @Rule public ExpectedException expected = ExpectedException.none();

    private final FuncitoDelegate delegate = new FuncitoDelegate();
    private A CALLS_TO_A = delegate.callsTo(A.class);

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

    private A a = new A();
    private B b = new B();
    private C c = new C();

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate.extractInvokableState(null);
        } catch (Throwable t) {}
    }

    @Test
    public void testNoOp() {
        CALLS_TO_A.getB().getC();

        UntypedMode mode = Modes.noOp();
        FunctorBase<A,C> functorBase = (FunctorBase<A,C>)mode.makeBase(getState());

        assertTrue(mode instanceof  NoOp);
        assertTrue(functorBase instanceof BasicFunctor);
    }

    @Test
    public void testTypedSafeNav() {
        CALLS_TO_A.getB().getC();

        C defaultC = new C();
        Mode<A,C> mode = Modes.safeNav(defaultC);
        FunctorBase<A,C> functorBase = mode.makeBase(getState());

        assertTrue(mode instanceof SafeNav);
        assertTrue(functorBase instanceof SafeNavFunctor);
        assertSame(defaultC, functorBase.applyImpl(a));
    }

    @Test
    public void testTypedSafeNav_NoDefaultNeeded() {
        CALLS_TO_A.getB().getC();
        a.setB(b);
        b.setC(c);
        C defaultC = new C();

        Mode<A,C> mode = Modes.safeNav(defaultC);
        FunctorBase<A,C> functorBase = mode.makeBase(getState());

        assertTrue(mode instanceof SafeNav);
        assertTrue(functorBase instanceof SafeNavFunctor);
        assertNotSame(defaultC, functorBase.applyImpl(a));
        assertSame(c, functorBase.applyImpl(a));
    }

    @Test
    public void testUntypedSafeNav_forNullDefault() {
        CALLS_TO_A.getB().getC();

        UntypedMode mode = Modes.safeNav();
        FunctorBase<A,C> functorBase = (FunctorBase<A,C>)mode.makeBase(getState());

        assertTrue(mode instanceof UntypedSafeNav);
        assertTrue(functorBase instanceof SafeNavFunctor);
        assertNull(functorBase.applyImpl(a));
    }

    @Test
    public void testUntypedSafeNav_noDefaultNeeded() {
        CALLS_TO_A.getB().getC();
        a.setB(b);
        b.setC(c);

        UntypedMode mode = Modes.safeNav();
        FunctorBase<A,C> functorBase = (FunctorBase<A,C>)mode.makeBase(getState());

        assertTrue(mode instanceof UntypedSafeNav);
        assertTrue(functorBase instanceof SafeNavFunctor);
        assertSame(c, functorBase.applyImpl(a));
    }

    @Test
    public void testTailDefault() {
        CALLS_TO_A.getB();
        B defaultB = new B();

        Mode<A,B> mode = Modes.tailDefault(defaultB);
        FunctorBase<A,B> functorBase = mode.makeBase(getState());

        assertTrue(mode instanceof TailDefault);
        assertTrue(functorBase instanceof TailDefaultFunctor);
        assertSame(defaultB, functorBase.applyImpl(a));
    }

    @Test
    public void testTailDefault_defaultNotNeeded() {
        CALLS_TO_A.getB();
        a.setB(b);
        B defaultB = new B();

        Mode<A,B> mode = Modes.tailDefault(defaultB);
        FunctorBase<A,B> functorBase = mode.makeBase(getState());

        assertTrue(mode instanceof TailDefault);
        assertTrue(functorBase instanceof TailDefaultFunctor);
        assertSame(b, functorBase.applyImpl(a));
    }

    @Test
    public void testTailDefault_doesNotHandleMiddleNulls() {
        CALLS_TO_A.getB().getC();
        C defaultC = new C();

        Mode<A,C> mode = Modes.tailDefault(defaultC);
        FunctorBase<A,C> functorBase = mode.makeBase(getState());

        expected.expect(FuncitoException.class);
        expected.expectMessage("NullPointerException");
        functorBase.applyImpl(a);
    }

    private InvokableState getState() {
        return delegate.extractInvokableState(null);
    }
}
