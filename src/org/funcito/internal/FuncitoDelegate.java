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
import org.funcito.internal.stub.StubFactory;

public class FuncitoDelegate {

    private static final ThreadLocal<InvocationManager> invocationManager = new ThreadLocal<InvocationManager>();
    
    public <T> T callsTo(Class<T> clazz) {
        return StubFactory.instance().stub(clazz);
    }

    private InvocationManager getManager() {
        InvocationManager manager = invocationManager.get();
        if (manager==null) {
            manager = new InvocationManager();
            invocationManager.set(manager);
        }
        return manager;
    }

    //-------------------- Funcito Core -------------------------

    public void putInvokable(Invokable invokable) {
        getManager().pushInvokable(invokable);
    }

    public InvokableState extractInvokableState(WrapperType wrapperType) {
        InvokableState state = getManager().extractState();
        if (state.isEmpty()) {
            throw new FuncitoException("Failed to create a " + wrapperType + ".  No call to a Funcito stub object was registered.");
        }
        return state;
    }
}
