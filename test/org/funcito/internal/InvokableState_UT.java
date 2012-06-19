package org.funcito.internal;

import org.funcito.FuncitoException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Deque;
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
    private Invokable invokable2 = null;

    @Before
    public void setUp() throws NoSuchMethodException {
        MockitoAnnotations.initMocks(this);
        invokable  = new Invokable<Deque,Object>(Deque.class.getMethod("pop"), Deque.class, true);
        invokable2 = new Invokable<Object,String>(Object.class.getMethod("toString"), Object.class, false);
    }

    @Test
    public void testPut_Basic() {
        // test
        state.put(invokable);
        assertTrue(state.isPopulated());
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

    @Test
    public void testIterator_multiWhenChainable() {
        assertTrue(invokable.isChainable());
        state.put(invokable);
        state.put(invokable2);

        Iterator<Invokable> iter = state.iterator();

        assertTrue(iter.hasNext());
        assertSame(invokable, iter.next());

        assertTrue(iter.hasNext());
        assertSame(invokable2, iter.next());
        assertFalse(iter.hasNext());
    }


    @Test(expected = FuncitoException.class)
    public void testPut_multiWhenUnchainable() {
        assertFalse(invokable2.isChainable());
        state.put(invokable2);
        // put
        state.put(invokable);
    }
}
