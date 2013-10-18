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

/**
 * This functor modifies basic execution of the InvokableState, by checking the final computed return value to see if
 * it is null and providing an alternate default value instead.  It is internally used by predicates in 3rd party APIs
 * that have non-primitive Boolean return types.  Since predicates by nature are 2-state for filtering actions, the 3rd
 * possible state of null is undesirable, and this functor provides a way to ensure that a suitable default can
 * substitute for null.  The <code>tailDefault()</code> TypedMode can also be used explicitly to provide this functor
 * implementation for <em>any</em> functor, whether or not it is a predicate.
 * <p/>
 * Note that this is different from the <code>TailDefaultFunctor</code>, which only checks the tail-result for null, but
 * <em>not</em> any intermediate null values in method-chains.
 * @param <T> The target (input) type of the functor
 * @param <V> The output type of the functor
 * @see TailDefaultFunctor
 */
public class SafeNavFunctor<T,V> extends AbstractFunctorBase<T,V> {
    final private Object nullNavDefault;

    /**
     * @param state the invokable state
     * @param nullNavDefault the default value to be returned in case any method return value is null.
     */
    public SafeNavFunctor(InvokableState state, V nullNavDefault) {
        super(state);
        this.nullNavDefault = nullNavDefault;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V applyImpl(T from) {
        if (from==null) {
            return (V) nullNavDefault;
        }
        // unroll the first loop, to provide performance for the "90%": unchained wrapped methods
        Object retVal = firstInvokable.invoke(from);
        if (retVal==null) {
            return (V) nullNavDefault;
        }
        if (unchained) {
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
        return (V)retVal;
    }

}
