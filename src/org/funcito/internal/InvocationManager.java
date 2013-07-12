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

class InvocationManager {

    private InvokableState state = new InvokableState();

    void pushInvokable(Invokable invokable) {
        state.put(invokable);
    }

    /**
     * Non-destructive peek at head Invokable
     * @return Invokable that is at the head of the InvokableState
     */
    protected Invokable peekInvokable() {
        return state.peek();
    }

    public InvokableState extractState() {
        InvokableState oldState = this.state;
        state = new InvokableState();
        return oldState;
    }
}
