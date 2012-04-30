package org.funcito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.funcito.FuncitoException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
        invokable = new Invokable<Object,String>(Class.class.getMethod("getName"), Object.class);
    }

    @Test
    public void testPut_Basic() {
        // test
        state.put(invokable);
        assertTrue(state.isFull());
    }

    @Test
    public void testGet_Basic() {
        state.put(invokable);
        
        // test
        Invokable result = state.get();
        
        assertTrue(state.isEmpty());
        assertEquals(invokable, result);
    }
    
    @Test(expected = FuncitoException.class)
    public void testGet_Empty() {
        // test
        state.get();
    }

    @Test(expected = FuncitoException.class)
    public void testGet_Twice() {
        state.put(invokable);
        assertTrue(state.isFull());
        state.get();        
        // test
        state.get();
    }
    
    @Test(expected = FuncitoException.class)
    public void testPut_Twice() {
        state.put(invokable);
        // put
        state.put(invokable);
    }

}
