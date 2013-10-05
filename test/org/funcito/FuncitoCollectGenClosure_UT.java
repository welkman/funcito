package org.funcito;

import org.apache.commons.collections15.Closure;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.funcito.FuncitoCollectGen.*;
import static org.funcito.mode.Modes.safeNav;
import static org.junit.Assert.*;

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

public class FuncitoCollectGenClosure_UT {

    public @Rule ExpectedException thrown = ExpectedException.none();
    private Grows CALLS_TO_GROWS = callsTo(Grows.class);

    @After
    public void tearDown() {
        try {
            new FuncitoDelegate().extractInvokableState(WrapperType.JEDI_VOID_COMMAND);
        } catch (Throwable t) {}
    }

    public class Grows {
        int i;
        public Grows(int i) { this.i = i; };
        public Grows() { this(0); };
        public Grows incAndReturn() {
            ++i;
            return this;
        }
        public void inc() { i++; }
        public void dec() { i--; }
    }

    @Test
    public void testClosureFor_AssignToClosureWithMatchingSourceType() {
        Grows grows = new Grows();

        Closure<Grows> superTypeRet = closureFor(CALLS_TO_GROWS.incAndReturn()); // happy path
        assertEquals(0, grows.i);

        superTypeRet.execute(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testClosureFor_AssignToClosureWithSourceSuperType() {
        Grows grows = new Grows();

        Closure<Object> superTypeRet = closureFor(CALLS_TO_GROWS.incAndReturn()); // Generic type is Object instead of Grows
        assertEquals(0, grows.i);

        superTypeRet.execute(grows);
        assertEquals(1, grows.i);
    }

    class Generic<N extends Number> {
        Double number;
        public Generic(N n) { number = n.doubleValue(); }
        public Double incAndGet() { return ++number; }
        public void voidInc() { ++number; }
    }

    @Test
    public void testClosureFor_AllowUpcastToExtensionGenericType() {
        Closure<Generic<? extends Object>> incClosure = closureFor(callsTo(Generic.class).incAndGet());
        Generic<Integer> integerGeneric = new Generic<Integer>(0);
        assertEquals(0, integerGeneric.number, 0.01);

        incClosure.execute(integerGeneric);

        assertEquals(1.0, integerGeneric.number, 0.01);
    }

    @Test
    public void testClosureFor_TypedAndUntypedModes() {
        Closure<Grows> closure = closureFor(CALLS_TO_GROWS.incAndReturn(), safeNav());
        // Without safeNav() untyped Mode, this results in a NPE
        closure.execute(null);

        closure = closureFor(CALLS_TO_GROWS.incAndReturn(), safeNav((Void)null));
        // Without safeNav() TypedMode, this results in a NPE
        closure.execute(null);
    }

    @Test
    public void testVoidClosure_withPrepare() {
        Grows grows = new Grows();

        prepareVoid(CALLS_TO_GROWS).inc();
        Closure<Grows> normalCall = voidClosure();

        assertEquals(0, grows.i);
        normalCall.execute(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidClosure_withoutPrepare() {
        Grows grows = new Grows();

        // non-preferred.  Better to use prepare() to help explain
        CALLS_TO_GROWS.inc();
        Closure<Grows> normalCall = voidClosure();

        assertEquals(0, grows.i);
        normalCall.execute(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidClosure_prepareWithNoMethodCall() {
        prepareVoid(CALLS_TO_GROWS); // did not append any ".methodCall()" after close parenthesis

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("No call to a");
        Closure<Grows> badCall = voidClosure();
    }

    @Test
    public void testVoidClosure_AssignToClosureWithSourceSuperTypeOk() {
        Grows grows = new Grows();

        prepareVoid(CALLS_TO_GROWS).inc();
        Closure<Object> superTypeRet = voidClosure(); // Generic type is Object instead of Grows
        assertEquals(0, grows.i);

        superTypeRet.execute(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidClosure_unsafeAssignment() {
        prepareVoid(CALLS_TO_GROWS).inc();
        Closure<Integer> unsafe = voidClosure();  // unsafe assignment compiles

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Method inc() does not exist");
        unsafe.execute(3); // invocation target type does not match prepared target type
    }

    @Test
    public void testVoidClosure_badOrderOfPrepares() {
        // First call below requires a subsequent call to voidClosure() before another prepareVoid()
        prepareVoid(CALLS_TO_GROWS).inc();

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("or back-to-back \"prepareVoid()\" calls");
        // bad to call prepareVoid() twice without intermediate voidClosure()
        prepareVoid(CALLS_TO_GROWS).dec();
        // see clean-up in method tearDown()
    }

    @Test
    public void testVoidClosure_interleavedPreparesFromDifferentSourcesAlsoNotOk() {
        prepareVoid(CALLS_TO_GROWS).inc();

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("or back-to-back \"prepareVoid()\" calls");
        prepareVoid(callsTo(Generic.class)).voidInc();
    }

    @Test
    public void testVoidClosure_preparedForNonVoidMethodIsOk() {
        Grows grows = new Grows();
        assertEquals(0, grows.i);

        prepareVoid(CALLS_TO_GROWS).incAndReturn();
        Closure<Grows> e = voidClosure();

        e.execute(grows);

        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidClosure_TypedAndUntypedModes() {
        prepareVoid(CALLS_TO_GROWS).incAndReturn();
        Closure<Grows> closure = voidClosure(safeNav());
        // Without safeNav() untyped Mode, this results in a NPE
        closure.execute(null);

        prepareVoid(CALLS_TO_GROWS).incAndReturn();
        closure = voidClosure(safeNav((Void)null));
        // Without safeNav() TypedMode, this results in a NPE
        closure.execute(null);
    }
}

