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
package org.funcito.jedi;

import jedi.functional.Functor;
import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;

import java.util.Iterator;

public class MethodFunctor<T, V> implements Functor<T,V> {
    final private InvokableState state;
    private Invokable<T,V> firstInvokable;
    private boolean unchained;

    public MethodFunctor(InvokableState state) {
        this.state = state;
        Iterator<Invokable> iter = state.iterator();
        this.firstInvokable = iter.next(); // for efficiency for unchained invocations, extract ahead of time
        this.unchained = !iter.hasNext();
    }

    public V execute(T from) {
        Object retVal = (V) firstInvokable.invoke(from);
        if (unchained) {
            return (V)retVal;
        }
        Iterator<Invokable> iter = state.iterator();
        iter.next(); // skip the head which has already been processed
        while (iter.hasNext()) {
            retVal = iter.next().invoke(retVal);
        }
        return (V)retVal;
    }
}
