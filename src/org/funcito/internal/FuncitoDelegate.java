/*
 * Copyright 2011 Project Funcito Contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.funcito.internal;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.ProxyFactory;

public class FuncitoDelegate {

    private static final ThreadLocal<InvocationManager> invocationManager = new ThreadLocal<InvocationManager>();
    
    /**
     * Generates a proxy object for use with other Funcito function-object generating static calls.  This proxy should not
     * be used for any other purposes.  An example of proper usage (using Google Guava) is as follows:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Function<MyClass,RetType> func = functionFor( callsTo.noArgMethodWithRetType() );
     * </code>
     * <p>
     * In the above example, the limitation is that MyClass has to be proxyable by the current Proxy provider
     * (see {@link org.funcito.Funcito#FUNCITO_PROXY_PROVIDER_PROP}).  Generally this means interfaces, or non-private non-static
     * non-final classes.
     */
    public <T> T callsTo(Class<T> clazz) {
        return ProxyFactory.instance().proxy(clazz);
    }

    private InvocationManager getManager() {
        InvocationManager manager = invocationManager.get();
        if (manager==null) {
            manager = new InvocationManager();
            invocationManager.set(manager);
        }
        return manager;
    }

    protected <T> void validatePreparedVoidCall(Class<T> validationTargetClass, WrapperType genType) {
        Invokable head = getManager().peekInvokable();
        Class invokableTargetClass = head.getTarget().getClass();
        if (!validationTargetClass.isAssignableFrom(invokableTargetClass)) {
            throw new FuncitoException("Failed to create " + genType + " with validating void generator, because " +
                    "validationTargetClass " + validationTargetClass.getName() + " is not assignable from the head " +
                    "of the prepared invokable state.");
        }
    }

    //-------------------- Funcito Core -------------------------

    public void putInvokable(Invokable invokable) {
        getManager().pushInvokable(invokable);
    }

    public InvokableState extractInvokableState(WrapperType wrapperType) {
        InvokableState state = getManager().extractState();
        if (state.isEmpty()) {
            throw new FuncitoException("Failed to create a " + wrapperType + ".  No call to a Funcito proxy object was registered.");
        }
        return state;
    }
}
