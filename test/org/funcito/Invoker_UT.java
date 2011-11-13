package org.funcito;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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
public class Invoker_UT {
    @Test
    public void testInvoke_exceptionHandling() throws Throwable {
        class Thing{}
        Invokable<Thing,String> invokable = mock(Invokable.class);
        when(invokable.invoke(any(Thing.class), anyString())).thenThrow(new Exception("Blah"));
        when(invokable.getMethodName()).thenReturn("someMethod");
        Invoker<Thing,String> invoker = new Invoker<Thing, String>(invokable);

        try {
            invoker.apply(new Thing());
            fail("Bad!");
        } catch (FuncitoException e) {
            assertEquals("error applying Funcito Invokable for Method org.funcito.Invoker_UT$1Thing.someMethod()", e.getMessage());
        }
    }
}
