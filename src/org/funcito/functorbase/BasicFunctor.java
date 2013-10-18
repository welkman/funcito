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
 * A basic implementation of the FunctorBase interface, with optimizations for an InvokableState of a single
 * non-chained method call.  This is the functor variety produced by the <code>NoOp</code> mode.  It does not alter
 * or intervene in the natural execution of the InvokableState, and so it is also the most efficient of the Funcito
 * provided functors.
 * @param <T> The target (input) type of the functor
 * @param <V> The output type of the functor
 */
public class BasicFunctor<T, V> extends AbstractFunctorBase<T,V> {

    @SuppressWarnings("unchecked")
    public BasicFunctor(InvokableState state) {
        super(state);
    }

    /**
     * @param from the target (input) value to apply the functor to
     * @return the result of applying the functor
     */
    @Override
    @SuppressWarnings("unchecked")
    public V applyImpl(T from) {
        // unroll the first loop, to provide performance for the "90%": unchained wrapped methods
        Object retVal = firstInvokable.invoke(from);
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
