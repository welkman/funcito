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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO: probably write a UT class, especially, re: chaining and null Invokable
// TODO: probably rename InvokablesChain
public class InvokableState {
    private List<Invokable> invokablesList = new ArrayList<Invokable>();

    void put(Invokable invokable) {
        validate(invokable);
        invokablesList.add(invokable);
    }

    private void validate(Invokable invokable) {
        if (invokable == null) {
            throw new FuncitoException("Internal error: attempt to 'put' null invokable");
        }

        if (!isEmpty()) {
            Object tail = invokablesList.get(invokablesList.size()-1).getRetVal();
            if (tail!=invokable.getTarget()) {
                throw new FuncitoException("Registered a proxy method call that was not chained to the result " +
                        "of previous proxy method call.\n  This was likely due to one or more accidental calls to a Funcito proxy" +
                        " outside of one of the \"functionFor()\"-like methods");
            }
        }
    }

    boolean isPopulated() {
        return !invokablesList.isEmpty();
    }

    public Iterator<Invokable> iterator() { return invokablesList.iterator(); }
    
    public boolean isEmpty() {
        return invokablesList.isEmpty();
    }
}
