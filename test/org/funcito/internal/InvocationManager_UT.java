package org.funcito.internal;

import net.sf.cglib.asm.Type;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.MethodProxy;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.cglib.CglibInvokable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private MethodProxy mProxy;
    @Mock
    private Signature sig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mProxy.getSignature()).thenReturn(sig);
    }

    @Test(expected = FuncitoException.class)
    public void testPushInvocation_nonZeroArgsNotAllowed() {
        when(sig.getArgumentTypes()).thenReturn(new Type[1]);
        mgr.pushInvokable(new CglibInvokable(mProxy, Object.class));
    }

    @Test(expected = FuncitoException.class)
    public void testPushInvocation_multiCglibCallsNotAllowed() {
        when(sig.getArgumentTypes()).thenReturn(new Type[0]);
        mgr.pushInvokable(new CglibInvokable(mProxy, Object.class));
        mgr.pushInvokable(new CglibInvokable(mProxy, Object.class));
    }
    
    @Test(expected = FuncitoException.class)
    public void testExtractInvokable_cannotCallWhenEmpty() {
        mgr.extractInvokable("");
    }

    @Test
    public void testExtractInvokable_cglibInvokableWrapsInvocationMethodProxy() throws Throwable {
        when(sig.getArgumentTypes()).thenReturn(new Type[0]);
        CglibInvokable<?, ?> invocation = new CglibInvokable<Object, Object>(mProxy, Object.class);
        mgr.pushInvokable(invocation);

        CglibInvokable<?, ?> invokable = (CglibInvokable<?, ?>) mgr.extractInvokable("");

        invokable.invoke(null); // verify that the invokable wraps the MethodProxy from pushed Invocation
        verify(mProxy).invoke(null, new Object[0]);
    }
}
