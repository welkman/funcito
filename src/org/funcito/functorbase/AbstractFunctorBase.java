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
 * An abstract partial implementation of the FunctorBase interface, with internal state useful for optimizing
 * executing a single non-chained method call.  It is used as the base for all of Funcito's provided basic
 * functor implementations.
 * @param <T> The target (input) type of the functor
 * @param <V> The output type of the functor
 */
public abstract class AbstractFunctorBase<T,V> implements FunctorBase<T,V> {
    final protected InvokableState state;
    final protected Invokable<T,?> firstInvokable;
    final protected boolean unchained;

    @SuppressWarnings("unchecked")
    public AbstractFunctorBase(InvokableState state) {
        this.state = state;
        Iterator<Invokable> iter = state.iterator();
        // for performance of unchained invocations, extract ahead of time
        firstInvokable = iter.next();
        unchained = !iter.hasNext();
    }
}
