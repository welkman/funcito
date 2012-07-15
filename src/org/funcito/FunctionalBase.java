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

import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;

import java.util.Iterator;

public class FunctionalBase<T, V> {
    final protected InvokableState state;
    final protected Invokable<T,?> firstInvokable;
    final protected boolean unchained;

    public FunctionalBase(InvokableState state) {
        this.state = state;
        Iterator<Invokable> iter = state.iterator();
        this.firstInvokable = iter.next(); // for performance for unchained invocations, extract ahead of time
        this.unchained = !iter.hasNext();
    }

    public V applyImpl(T from) {
        // unroll the first loop, to provide performance for the "90%": unchained wrapped methods
        Object retVal = firstInvokable.invoke(from);
        if (unchained) {
            validateReturnValue(retVal);
            return (V)retVal;
        }
        Iterator<Invokable> iter = state.iterator();
        iter.next(); // skip the head which has already been processed
        while (iter.hasNext()) {
            retVal = iter.next().invoke(retVal);
        }
        validateReturnValue(retVal);
        return (V)retVal;
    }

    protected void validateReturnValue(Object retVal) {}; //default action is nothing

}
