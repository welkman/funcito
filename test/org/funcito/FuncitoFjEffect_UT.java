package org.funcito;

import fj.Effect;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.funcito.FuncitoFJ.*;
import static org.junit.Assert.*;

public class FuncitoFjEffect_UT {

    public @Rule ExpectedException expected = ExpectedException.none();
    private Grows CALLS_TO_GROWS = callsTo(Grows.class);

    @After
    public void tearDown() {
        try {
            new FuncitoDelegate().extractInvokableState(WrapperType.FJ_VOID_EFFECT);
        } catch (Throwable t) {}
    }

    public class Grows {
        int i = 0;
        public String incAndReturn() {
            i++;
            return Integer.toString(i);
        }
        public void inc() {
            i++;
        }
        public void dec() {
            i--;
        }
    }

    @Test
    public void testNonVoidEffectFor_AssignToEffectWithSourceSuperType() {
        Grows grows = new Grows();

        Effect<Object> superTypeRet = effectFor(CALLS_TO_GROWS.incAndReturn()); // Generic type is Object instead of Grows
        assertEquals(0, grows.i);

        superTypeRet.e(grows);
        assertEquals(1, grows.i);
    }

    class Generic<N extends Number> {
        Double number;
        public Generic(N n) { number = n.doubleValue(); }
        public Double incAndGet() {
            number = number.doubleValue() + 1;
            return number; }
    }

    @Test
    public void testNonVoidEffectFor_ValidateDetectsMismatchedGenericTypes() {
        Effect<Generic<Float>> floatGenericEffect = effectFor(callsTo(Generic.class).incAndGet());
        Generic<Integer> integerGeneric = new Generic<Integer>(0);

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        floatGenericEffect.f(integerGeneric);
    }

    @Test
    public void testNonVoidEffectFor_AllowUpcastToExtensionGenericType() {
        Effect<Generic<? extends Object>> incEffect = effectFor(callsTo(Generic.class).incAndGet());
        Generic<Integer> integerGeneric = new Generic<Integer>(0);
        assertEquals(0, integerGeneric.number, 0.01);

        incEffect.e(integerGeneric);

        assertEquals(1.0, integerGeneric.number, 0.01);
    }

    @Test
    public void testNonVoidEffectFor_SingleArgBinding() {
        class IncList extends ArrayList<Integer> {
            public int incIndex(int i) {
                int oldVal = this.get(i).intValue();
                int newVal = oldVal + 1;
                this.set(i, newVal);
                return newVal;
            }
        }
        IncList callsToIncList = callsTo(IncList.class);
        Effect<IncList> incElem0Func = effectFor(callsToIncList.incIndex(0));
        Effect<IncList> incElem2Func = effectFor(callsToIncList.incIndex(2));
        IncList list = new IncList();
        list.add(0); list.add(100); list.add(1000);

        incElem0Func.e(list);
        incElem2Func.e(list);

        assertEquals(1, list.get(0).intValue());
        assertEquals(100, list.get(1).intValue()); // unchanged
        assertEquals(1001, list.get(2).intValue());
    }

    @Test
    public void testVoidEffect_withPrepare() {
        Grows grows = new Grows();

        prepareVoid(CALLS_TO_GROWS).inc();
        Effect<Grows> normalCall = voidEffect();

        assertEquals(0, grows.i);
        normalCall.e(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidEffect_withoutPrepare() {
        Grows grows = new Grows();

        // non-preferred.  Better to use prepare() to help explain
        CALLS_TO_GROWS.inc();
        Effect<Grows> normalCall = voidEffect();

        assertEquals(0, grows.i);
        normalCall.e(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidEffect_AssignToEffectWithSourceSuperType() {
        Grows grows = new Grows();

        prepareVoid(CALLS_TO_GROWS).inc();
        Effect<Object> superTypeRet = voidEffect(); // Generic type is Object instead of Grows
        assertEquals(0, grows.i);

        superTypeRet.e(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidEffect_badOrderOfPrepares() {
        // First call below requires a subsequent call to voidEffect() before another prepareVoid()
        prepareVoid(CALLS_TO_GROWS).inc();

        expected.expect(FuncitoException.class);
        expected.expectMessage("or back-to-back \"prepareVoid()\" calls");
        // bad to call prepareVoid() twice without intermediate voidEffect()
        prepareVoid(CALLS_TO_GROWS).dec();
        // see clean-up in method tearDown()
    }
}

