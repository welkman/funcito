/*
 * Copyright 2013 Project Funcito Contributors
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
package org.funcito.functorbase;

import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;

import java.util.Iterator;

public class SafeNavFunctor<T,V> extends BasicFunctor<T,V> {
    final private Object nullNavDefault;

    public SafeNavFunctor(InvokableState state, V nullNavDefault) {
        super(state);
        this.nullNavDefault = nullNavDefault;
    }

    @SuppressWarnings("unchecked")
    public V applyImpl(T from) {
        // unroll the first loop, to provide performance for the "90%": unchained wrapped methods
        Object retVal = firstInvokable.invoke(from);
        if (retVal==null) {
            return (V) nullNavDefault;
        }
        if (unchained) {
            validateReturnValue(retVal); // TODO: determine if needed, & whether to deprecate NullValidatingPredicate
            return (V)retVal;
        }
        Iterator<Invokable> iter = state.iterator();
        iter.next(); // skip the head which has already been processed
        while (iter.hasNext()) {
            retVal = iter.next().invoke(retVal);
            if (retVal==null) {
                return (V) nullNavDefault;
            }
        }
        validateReturnValue(retVal); // TODO: determine if needed
        return (V)retVal;
    }

}
