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
 * This functor is roughly modelled after the Groovy language safe-navigation operator: <b>?.</b>  The main difference
 * is that once-applied to the head of a method call-chain, it implicitly applies to all remaining chained elements in
 * the expression as well.
 * <p/>
 * This functor modifies basic execution of the InvokableState, by checking each intermediate term in a wrapped call-chain
 * to see if it results in null.  If so rather than letting that null cause a NullPointerException when chained by a
 * subsequent method call, it instead safely returns an alternative default value.  Note that this is not implemented as
 * a NullPointerException checker.  So a NullPointerException that is thrown internally to any of the methods in the
 * chain will still propagate out.
 * <p/>
 * Note that this differs from the <code>TailDefaultFunctor</code>, which only checks the tail-result for null, but
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
