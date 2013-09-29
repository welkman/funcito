/*
 * Copyright 2011-2013 Project Funcito Contributors
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
import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;
import org.funcito.functorfactory.FunctorFactory;
import org.funcito.mode.Mode;
import org.funcito.mode.TypedMode;

public class JediFunctor<T, V> implements Functor<T,V> {

    private FunctorBase<T,V> functorBase;

    public JediFunctor(InvokableState state, TypedMode<T,V> mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    public JediFunctor(InvokableState state, Mode mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    @Override
    public V execute(T from) {
        return functorBase.applyImpl(from);
    }
}
