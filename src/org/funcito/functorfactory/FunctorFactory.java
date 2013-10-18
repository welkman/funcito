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
package org.funcito.functorfactory;

import org.funcito.functorbase.BasicFunctor;
import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;
import org.funcito.mode.TypedMode;
import org.funcito.mode.Mode;

/**
 * Used by the constructors of the Funcito wrapper-classes for each 3rd party functor class
 */
public class FunctorFactory {
    private static final FunctorFactory INSTANCE = new FunctorFactory();

    private FunctorFactory() {}

    /**
     * Retrieves the singleton FunctorFactory
     */
    public static FunctorFactory instance() { return INSTANCE; }

    /**
     * Factory method for functors which use a <code>TypedMode</code>
     * @param state the captured and extracted invokable state to be transformed into a FunctorBase
     * @param mode the TypedMode that determines how the FunctorBase will behave
     * @param <T> the target (input) type of the resulting FunctorBase
     * @param <V> the resulting (output) type of the resulting FunctorBase
     * @return a FunctorBase that will execute/replay the captured InvokableState of method calls
     */
    @SuppressWarnings("unchecked")
    public <T,V> FunctorBase<T,V> makeFunctionalBase(InvokableState state, TypedMode<V> mode) {
        if (mode != null) {
            return (FunctorBase<T, V>) mode.makeBase(state);
        }
        return new BasicFunctor<T,V>(state);
    }

    /**
     * Factory method for functors which use an untyped <code>Mode</code>
     * @param state the captured and extracted invokable state to be transformed into a FunctorBase
     * @param mode the  untyped Mode that determines how the FunctorBase will behave
     * @param <T> the target (input) type of the resulting FunctorBase
     * @param <V> the resulting (output) type of the resulting FunctorBase
     * @return a FunctorBase that will execute/replay the captured InvokableState of method calls
     */
    @SuppressWarnings("unchecked")
    public <T,V> FunctorBase<T,V> makeFunctionalBase(InvokableState state, Mode mode) {
        if (mode != null) {
            return (FunctorBase<T,V>)mode.makeBase(state);
        }
        return new BasicFunctor<T,V>(state);
    }
}
