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
package org.funcito;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import rx.functions.Action1;

import static org.funcito.FuncitoRxJava.*;
import static org.funcito.mode.Modes.safeNav;
import static org.junit.Assert.*;

public class FuncitoRxJavaAction1_UT {

    public @Rule ExpectedException thrown = ExpectedException.none();
    private Grows CALLS_TO_GROWS = callsTo(Grows.class);

    @After
    public void tearDown() {
        try {
            new FuncitoDelegate().extractInvokableState(WrapperType.RXJAVA_VOID_ACTION1);
        } catch (Throwable t) {}
    }

    public class Grows {
        int i = 0;
        public Grows incAndReturn() {
            ++i;
            return this;
        }
        public void inc() { i++; }
        public void dec() { i--; }
    }

    @Test
    public void testAction1For_AssignToAction1WithMatchingSourceType() {
        Grows grows = new Grows();

        Action1<Grows> superTypeRet = action1For(CALLS_TO_GROWS.incAndReturn()); // Happy Path!
        assertEquals(0, grows.i);

        superTypeRet.call(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testAction1For_AssignToAction1WithSourceSuperType() {
        Grows grows = new Grows();

        Action1<Object> superTypeRet = action1For(CALLS_TO_GROWS.incAndReturn()); // Generic type is Object instead of Grows
        assertEquals(0, grows.i);

        superTypeRet.call(grows);
        assertEquals(1, grows.i);
    }

    class Generic<N extends Number> {
        Double number;
        public Generic(N n) { number = n.doubleValue(); }
        public Double incAndGet() { return ++number; }
        public void voidInc() { ++number; }
    }

    @Test
    public void testAction1For_AllowUpcastToExtensionGenericType() {
        Action1<Generic<? extends Object>> incAction1 = action1For(callsTo(Generic.class).incAndGet());
        Generic<Integer> integerGeneric = new Generic<Integer>(0);
        assertEquals(0, integerGeneric.number, 0.01);

        incAction1.call(integerGeneric);

        assertEquals(1.0, integerGeneric.number, 0.01);
    }

    @Test
    public void testEffectFor_TypedAndUntypedModes() throws Throwable {
        Action1<Grows> action1 = action1For(CALLS_TO_GROWS.incAndReturn(), safeNav());
        // Without safeNav() untyped Mode, this results in a NPE
        action1.call(null);

        action1 = action1For(CALLS_TO_GROWS.incAndReturn(), safeNav((Void)null));
        // Without safeNav() TypedMode, this results in a NPE
        action1.call(null);
    }

    @Test
    public void testVoidAction1_withPrepare() {
        Grows grows = new Grows();

        prepareVoid(CALLS_TO_GROWS).inc();
        Action1<Grows> normalAction = voidAction1();

        assertEquals(0, grows.i);
        normalAction.call(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidAction1_withoutPrepare() {
        Grows grows = new Grows();

        // non-preferred.  Better to use prepare() to help explain
        CALLS_TO_GROWS.inc();
        Action1<Grows> normalAction = voidAction1();

        assertEquals(0, grows.i);
        normalAction.call(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidAction1_prepareWithNoMethodCall() {
        prepareVoid(CALLS_TO_GROWS); // did not append any ".methodCall()" after close parenthesis

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("No call to a");
        Action1<Grows> badCall = voidAction1();
    }

    @Test
    public void testVoidAction1_AssignToAction1WithSourceSuperTypeOk() {
        Grows grows = new Grows();

        prepareVoid(CALLS_TO_GROWS).inc();
        Action1<Object> superTypeRet = voidAction1(); // Generic type is Object instead of Grows
        assertEquals(0, grows.i);

        superTypeRet.call(grows);
        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidAction1_unsafeAssignment() {
        prepareVoid(CALLS_TO_GROWS).inc();
        Action1<Integer> unsafe = voidAction1();  // unsafe assignment compiles

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Method inc() does not exist");
        unsafe.call(3); // invocation target type does not match prepared target type
    }

    @Test
    public void testVoidAction1_badOrderOfPrepares() {
        // First call below requires a subsequent call to voidAction1() before another prepareVoid()
        prepareVoid(CALLS_TO_GROWS).inc();

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("or back-to-back \"prepareVoid()\" calls");
        // bad to call prepareVoid() twice without intermediate voidAction1()
        prepareVoid(CALLS_TO_GROWS).dec();
        // see clean-up in method tearDown()
    }

    @Test
    public void testVoidAction1_interleavedPreparesDifferentSourcesAlsoNotOk() {
        prepareVoid(CALLS_TO_GROWS).inc();

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("or back-to-back \"prepareVoid()\" calls");
        prepareVoid(callsTo(Generic.class)).voidInc();
    }

    @Test
    public void testVoidAction1_preparedForNonVoidMethod() {
        Grows grows = new Grows();
        assertEquals(0, grows.i);

        prepareVoid(CALLS_TO_GROWS).incAndReturn();
        Action1<Grows> action = voidAction1();

        action.call(grows);

        assertEquals(1, grows.i);
    }

    @Test
    public void testVoidAction1_TypedAndUntypedModes() throws Throwable {
        prepareVoid(CALLS_TO_GROWS).incAndReturn();
        Action1<Grows> action1 = voidAction1(safeNav());
        // Without safeNav() untyped Mode, this results in a NPE
        action1.call(null);

        prepareVoid(CALLS_TO_GROWS).incAndReturn();
        action1 = voidAction1(safeNav((Void)null));
        // Without safeNav() TypedMode, this results in a NPE
        action1.call(null);
    }
}

