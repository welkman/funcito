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

import java.util.Stack;

import org.funcito.FuncitoException;

class InvocationManager {
    static final private Stack<Invokable> queuedInvokables = new Stack<Invokable>();

    void pushInvokable(Invokable invokable) {
        if (!isInvocationsEmpty()) {
            clearInvocations();
            throw new FuncitoException("A method call to a Funcito stub was detected outside of acceptable scope.  This usually means you attempted to invoke methods on the stub without wrapping it in one of the Funcito static wrapping methods.");
        }
        if (invokable.getArgumentsLength() != 0) {
            clearInvocations();
            throw new FuncitoException("Failed to wrap method invocation.  Signature of method call on Funcito stub has arguments, when only no-arg methods are wrappable.");
        }
        queuedInvokables.push(invokable);
    }

    Invokable extractInvokable(String wrapperType) {
        validateInvocationsCount(wrapperType);
        return queuedInvokables.pop();
    }

    private void validateInvocationsCount(String wrapperType) {
        if (isInvocationsEmpty()) {
            throw new FuncitoException("Failed to create a " + wrapperType + ".  No call to a Funcito stub object was registered.");
        }
    }

    private void clearInvocations() {
        queuedInvokables.clear();
    }

    private boolean isInvocationsEmpty() {
        return queuedInvokables.isEmpty();
    }

}
