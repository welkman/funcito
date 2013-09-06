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

import org.funcito.internal.InvokableState;
import org.funcito.modifier.Modifier;
import org.funcito.modifier.UntypedModifier;

// TODO: javadoc class
public class FunctorFactory {
    private static final FunctorFactory INSTANCE = new FunctorFactory();

    private FunctorFactory() {}

    // TODO: javadoc method
    public static FunctorFactory instance() { return INSTANCE; }

    // TODO: javadoc method
    public <T,V> FunctorBase<T,V> makeFunctionalBase(InvokableState state, Modifier<T,V> mod) {
        if (mod != null) {
            return mod.makeBase(state);
        }
        return new BasicFunctor<T,V>(state);
    }

    // TODO: javadoc method
    @SuppressWarnings("unchecked")
    public <T,V> FunctorBase<T,V> makeFunctionalBase(InvokableState state, UntypedModifier mod) {
        if (mod != null) {
            return (FunctorBase<T,V>)mod.makeBase(state);
        }
        return new BasicFunctor<T,V>(state);
    }
}
