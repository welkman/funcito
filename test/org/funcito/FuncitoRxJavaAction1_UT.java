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

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import rx.util.functions.Action1;

import java.util.ArrayList;

import static org.funcito.FuncitoRxJava.*;
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
        public String incAndReturn() {
            return Integer.toString(++i);
        }
        public void inc() {
            i++;
        }
        public void dec() {
            i--;
        }
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
        public Double incAndGet() {
            return ++number;
        }
        public void voidInc() {
            ++number;
        }
    }

    @Test
    public void testAction1For_ValidateDetectsMismatchedGenericTypes() {
        Action1<Generic<Float>> floatGenericAction1 = action1For(callsTo(Generic.class).incAndGet());
        Generic<Integer> integerGeneric = new Generic<Integer>(0);

//        The below can't actually be compiled, which proves the test passes: compile time mismatch detection
//        floatGenericAction1.call(integerGeneric);
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
    public void testAction1For_SingleArgBinding() {
        class IncList extends ArrayList<Integer> {
            public int incIndex(int i) {
                int oldVal = this.get(i);
                int newVal = oldVal + 1;
                this.set(i, newVal);
                return newVal;
            }
        }
        IncList callsToIncList = callsTo(IncList.class);
        Action1<IncList> incElem0Action = action1For(callsToIncList.incIndex(0));
        Action1<IncList> incElem2Action = action1For(callsToIncList.incIndex(2));
        IncList list = new IncList();
        list.add(0); list.add(100); list.add(1000);

        incElem0Action.call(list);
        incElem2Action.call(list);

        assertEquals(1, list.get(0).intValue());
        assertEquals(100, list.get(1).intValue()); // unchanged
        assertEquals(1001, list.get(2).intValue());
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
    public void testVoidAction1_typeValidationSucceeds() {
        prepareVoid(CALLS_TO_GROWS).inc();

        Action1<Grows> grows = voidAction1(Grows.class);
    }

    @Test
    public void testVoidAction1_typeValidationSucceedsWithSuperClass() {
        class Grows2 extends Grows{}
        prepareVoid(callsTo(Grows2.class)).inc();

        Action1<Grows> grows = voidAction1(Grows.class);
    }

    @Test
    public void testVoidAction1_typeValidationFails() {
        prepareVoid(CALLS_TO_GROWS).inc();

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Failed to create RxJava Action1");
        Action1<?> e = voidAction1(Number.class);  // type validation
    }


    @Test
    public void testVoidAction1_typeValidationFailsButLeavesInvokableStateUnchanged() {
        prepareVoid(CALLS_TO_GROWS).inc();

        try {
            Action1<?> e = voidAction1(Number.class);  // type validation should fail
            fail("should have thrown exception");
        } catch (FuncitoException e) {
            Action1<Grows> g = voidAction1(Grows.class);  // type validation
        }
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
}

