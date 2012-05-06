package org.funcito.internal;

import org.funcito.FuncitoException;
import org.junit.Test;

import java.lang.reflect.Method;
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

    public static final String DUMMY_ARG = "WrapperType string for error logging only";
    private InvocationManager mgr = new InvocationManager();

    @Test
    public void testPushInvocation_noArgMethodAllowed() throws NoSuchMethodException {
        Method method = List.class.getMethod("size");
        assertEquals(0, method.getParameterTypes().length);
        Invokable<List, Object> invokable = new Invokable<List, Object>(method, List.class);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractInvokable(DUMMY_ARG));
    }

    @Test
    public void testPushInvocation_oneArgMethodAllowed() throws NoSuchMethodException {
        Method method = List.class.getMethod("get", int.class );
        assertEquals(1, method.getParameterTypes().length);
        Invokable<List, Object> invokable = new Invokable<List, Object>(method, List.class, 0);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractInvokable(DUMMY_ARG));
    }

    @Test
    public void testPushInvocation_multiArgMethodAllowed() throws NoSuchMethodException {
        Method method = CharSequence.class.getMethod("subSequence", int.class, int.class );
        assertEquals(2, method.getParameterTypes().length);
        Invokable<CharSequence, CharSequence> invokable = new Invokable<CharSequence, CharSequence>(method, CharSequence.class, 1, 2);

        mgr.pushInvokable(invokable);

        assertSame(invokable, mgr.extractInvokable(DUMMY_ARG));
    }

    @Test(expected = FuncitoException.class)
    public void testPushInvocation_multiCallsNotAllowed() throws NoSuchMethodException {
        mgr.pushInvokable(new Invokable<Object,String>(Class.class.getMethod("getName"), Object.class));
        mgr.pushInvokable(new Invokable<Object,String>(Class.class.getMethod("getName"), Object.class));
    }
    
    @Test(expected = FuncitoException.class)
    public void testExtractInvokable_cannotCallWhenEmpty() {
        mgr.extractInvokable("");
    }
}
