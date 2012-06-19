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

class InvocationManager {

    private InvokableState state = new InvokableState();

    void pushInvokable(Invokable invokable) {
        if (!state.isAppendable()) {
            // TODO: maybe more intelligence in the error message(s) to output here, see also InvocationManager
            throwEx("A method call to a Funcito stub was detected outside of acceptable scope.  This likely means you attempted to invoke methods on the stub without wrapping it in one of the Funcito static wrapping methods.");
        }
        state.put(invokable);
    }

    public InvokableState extractState() {
        InvokableState oldState = this.state;
        state = new InvokableState();
        return oldState;
    }

    private void throwEx(String msg) {
        state.clear();
        throw new FuncitoException(msg);
    }
}
