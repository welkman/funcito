package org.funcito.internal;

import org.funcito.FuncitoException;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;

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

    @After
    public void tearDown() {
        try {
            new FuncitoDelegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    @Test
    public void testPushInvocation_noArgMethodAllowed() throws NoSuchMethodException {
        Method method = List.class.getMethod("size");
        assertEquals(0, method.getParameterTypes().length);
        Invokable<List, Object> invokable = new Invokable<List, Object>(method, new ArrayList(), 1);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractState().iterator().next());
    }

    @Test
    public void testPushInvocation_oneArgMethodAllowed() throws NoSuchMethodException {
        Method method = List.class.getMethod("get", int.class );
        assertEquals(1, method.getParameterTypes().length);
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

    @Test
    public void testPushInvocation_unchainedInvokablesNotAllowed() throws NoSuchMethodException {
        Method toString = Object.class.getMethod("toString");
        mgr.pushInvokable(new Invokable<Object,String>(toString, new Object(), "Object"));

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("proxy method call that was not chained to the result");
        // Invalid non-chained push, even though return type #1 is chainable and matches target type #2,
        // because object output by 1st call is not the same instance as input on second call
        mgr.pushInvokable(new Invokable<Object, String>(toString, new Object(), "Object"));
    }

    @Test
    public void testPushInvocation_nullInvokablesNotAllowed() throws NoSuchMethodException {
        thrown.expect(FuncitoException.class);
        thrown.expectMessage("null invokable");
        mgr.pushInvokable(null);
    }

    @Test
    public void testPushInvocation_validChainedPush() throws NoSuchMethodException {
        Method subSequence = CharSequence.class.getMethod("subSequence", int.class, int.class);
        CharSequence result1 = "ABC";
        Invokable<CharSequence,CharSequence> invokable1 = new Invokable<CharSequence,CharSequence>(subSequence, "ABCDE", result1, 0, 3);
        Invokable<CharSequence,CharSequence> invokable2 = new Invokable<CharSequence,CharSequence>(subSequence, result1, "BC", 1, 2);

        mgr.pushInvokable(invokable1);
        mgr.pushInvokable(invokable2);

        Iterator<Invokable> iterator = mgr.extractState().iterator();
        assertSame(invokable1, iterator.next());
        assertSame(invokable2, iterator.next());
        assertTrue(!iterator.hasNext());
    }

    @Test
    public void testExtractInvokable_canCallWhenEmpty() {
        InvokableState state = mgr.extractState();
        assertTrue(state.isEmpty());
    }
}
