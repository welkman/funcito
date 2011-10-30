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
package org.funcito;

import java.util.ArrayDeque;
import java.util.Deque;

public class InvocationManager {
    final private Deque<StubInvocation> queuedInvocations = new ArrayDeque<StubInvocation>();

    public void pushInvocation(StubInvocation invocation) {
        queuedInvocations.push(invocation);
    }

    public Invokable extractInvokable(String wrapperType) {
        validateInvocationsCount(wrapperType);
        final Invokable invokable = queuedInvocations.pop().getInvokable();
        clearInvocations();
        if (invokable.getArgumentsLength() != 0) {
            throw new RuntimeException("Failed to create a Function.  Method call on Function.stub() had arguments, when only no-arg methods are wrappable.");
        }
        return invokable;
    }

    private  void validateInvocationsCount(String wrapperType) {
        if (isInvocationsEmpty()) {
            throw new RuntimeException("Failed to create a " + wrapperType + ".  No call to a Funcito.stub() object was registered.");
        }
        if (queuedInvocations.size() > 1) {
            clearInvocations();
            throw new RuntimeException("Failed to create a " + wrapperType + ".  Multiple method calls to a Funcito.stub() object were registered during call to wrapAsMethod, when only one is allowed.  This may happen if you attempt to invoke methods on the stub outside of a Funcito.wrapAsMethod() call");
        }
    }

    private void clearInvocations() {
        queuedInvocations.clear();
    }

    private boolean isInvocationsEmpty() {
        return queuedInvocations.isEmpty();
    }

}
