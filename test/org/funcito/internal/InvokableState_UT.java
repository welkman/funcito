package org.funcito.internal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Iterator;

import static org.junit.Assert.*;

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
public class InvokableState_UT {

    private InvokableState state = new InvokableState();
    private Invokable invokable = null;

    @Before
    public void setUp() throws NoSuchMethodException {
        MockitoAnnotations.initMocks(this);
        Method method = CharSequence.class.getMethod("subSequence", int.class, int.class);
        invokable  = new Invokable<CharSequence,CharSequence>(method, "abc", "bc");
    }

    @Test
    public void testPut_Basic() {
        // test
        state.put(invokable);
        assertFalse(state.isEmpty());
    }

    @Test
    public void testPeek() {
        // test
        state.put(invokable);
        assertSame(invokable, state.peek());
        assertSame(invokable, state.peek()); // can be repeated
    }

    @Test
    public void testIterator_empty() {
        Iterator<Invokable> iter = state.iterator();
        
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIterator_one() {
        state.put(invokable);

        Iterator<Invokable> iter = state.iterator();

        assertTrue(iter.hasNext());
        assertEquals(invokable, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIterator_repeatable() {
        state.put(invokable);

        Iterator<Invokable> iter = state.iterator();

        assertTrue(iter.hasNext());
        assertEquals(invokable, iter.next());
        assertFalse(iter.hasNext());

        iter = state.iterator(); // get a fresh iterator

        assertTrue(iter.hasNext());
        assertEquals(invokable, iter.next());
        assertFalse(iter.hasNext());
    }
}
