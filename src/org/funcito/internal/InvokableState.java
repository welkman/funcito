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

// TODO: probably rename InvokablesChain
public class InvokableState {
    private List<Invokable> invokablesList = new ArrayList<Invokable>();

    void put(Invokable invokable) {
        if (invokable == null) {
            throw new FuncitoException("Internal error: attempt to 'put' null invokable");
        }
        if (!isAppendable()){
            // TODO: maybe more intelligence in the error message(s) to output here, see also InvocationManager
            throw new FuncitoException("Internal error: consecutive attempts to 'put' invokable");
        }

        invokablesList.add(invokable);
    }

    public boolean isAppendable() {
        return isEmpty() || invokablesList.get(invokablesList.size()-1).isChainable();
    }

    boolean isPopulated() {
        return !invokablesList.isEmpty();
    }

    public Iterator<Invokable> iterator() { return invokablesList.iterator(); }
    
    public boolean isEmpty() {
        return invokablesList.isEmpty();
    }
    
    void clear() {
        invokablesList.clear();
    }
}
