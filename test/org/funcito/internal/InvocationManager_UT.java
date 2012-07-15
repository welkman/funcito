package org.funcito.internal;

import com.google.common.base.Function;
import org.funcito.FuncitoException;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.funcito.FuncitoGuava.callsTo;
import static org.funcito.FuncitoGuava.functionFor;

/**
 * Copyright 2011 Project Funcito Contributors
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
public class InvocationManager_UT {

    private InvocationManager mgr = new InvocationManager();

    @Rule public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testPushInvocation_noArgMethodAllowed() throws NoSuchMethodException {
        Method method = List.class.getMethod("size");
        assertEquals(0, method.getParameterTypes().length);
//        Invokable<List, Object> invokable = new Invokable<List, Object>(method, List.class, false);
        Invokable<List, Object> invokable = new Invokable<List, Object>(method, new ArrayList(), 1);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractState().iterator().next());
    }

    @Test
    public void testPushInvocation_oneArgMethodAllowed() throws NoSuchMethodException {
        Method method = List.class.getMethod("get", int.class );
        assertEquals(1, method.getParameterTypes().length);
//        Invokable<List, Object> invokable = new Invokable<List, Object>(method, List.class, true, 0);
        Invokable<List, Object> invokable = new Invokable<List, Object>(method, new ArrayList(), 1, 0);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractState().iterator().next());
    }

    @Test
    public void testPushInvocation_multiArgMethodAllowed() throws NoSuchMethodException {
        Method method = CharSequence.class.getMethod("subSequence", int.class, int.class );
        assertEquals(2, method.getParameterTypes().length);
        Invokable<CharSequence, CharSequence> invokable = new Invokable<CharSequence, CharSequence>(method, "abc", "xyz", 1, 2);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractState().iterator().next());
    }

    @After
    public void tearDown() {
        try {
            new FuncitoDelegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    // TODO: move and/or reformulate for either InvocationManager or InvokableState
    @Test
    public void testPushInvocation_unchainedInvokablesNotAllowed() throws NoSuchMethodException {
        Method toString = Object.class.getMethod("toString");
        mgr.pushInvokable(new Invokable<Object,String>(toString, new Object(), "Object"));

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("proxy method call that was not chained to the result ");
        // Invalid non-chained push, even though return type #1 is chainable and matches target type #2,
        // because object output by 1st call is not the same instance as input on second call
        mgr.pushInvokable(new Invokable<Object, String>(toString, new Object(), "Object"));
    }

    // TODO: move and/or reformulate for either InvocationManager or InvokableState
    @Test
    public void testValidChainedPush() {
        // what we want to be able to distinguish is valid chained pushes...
        CharSequence charSequence = callsTo(CharSequence.class);
        Function<CharSequence,CharSequence> validF = functionFor(charSequence.subSequence(0, 3).subSequence(1,1));
    }

    // TODO: move and/or reformulate for either InvocationManager or InvokableState
    @Test
    public void test2() {
        // Theoretically, below is valid, ewen though it is non-standard use of API.
        CharSequence intermediate = callsTo(CharSequence.class).subSequence(0, 3);
        CharSequence validResult = intermediate.subSequence(1, 1);

        // test: success if no exception (or maybe the above call is the test, or maybe both?)
        functionFor(validResult);
    }

    @Test
    public void testExtractInvokable_canCallWhenEmpty() {
        InvokableState state = mgr.extractState();
        assertTrue(state.isEmpty());
    }
}
