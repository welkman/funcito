package org.funcito.mode;

import org.funcito.functorbase.BasicFunctor;
import org.funcito.functorbase.FunctorBase;
import org.funcito.functorbase.SafeNavFunctor;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class Modes_UT {

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

        assertSame(NoOp.NO_OP, mode);
        functorBase.getClass().equals(BasicFunctor.class);
    }

    @Test
    public void testTypedSafeNav() {
        CALLS_TO_A.getB().getC();

        C defaultC = new C();
        Mode<A,C> mode = Modes.safeNav(defaultC);
        FunctorBase<A,C> functorBase = mode.makeBase(getState());

        assertTrue(functorBase instanceof SafeNavFunctor);
        assertSame(defaultC, functorBase.applyImpl(new A()));
    }

    @Test
    public void testUntypedSafeNav_forNullDefault() {
        CALLS_TO_A.getB().getC();

        UntypedMode mode = Modes.safeNav();
        FunctorBase<A,C> functorBase = (FunctorBase<A,C>)mode.makeBase(getState());

        assertSame(UntypedSafeNav.SAFE_NAV, mode);
        assertTrue(functorBase instanceof SafeNavFunctor);
        assertNull(functorBase.applyImpl(new A()));
    }

    // TODO: need test for defaultBool()

    private InvokableState getState() {
        return delegate.extractInvokableState(null);
    }
}
