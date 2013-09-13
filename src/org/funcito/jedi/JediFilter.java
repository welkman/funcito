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

import jedi.functional.Filter;
import org.funcito.functorbase.BasicFunctor;
import org.funcito.functorbase.FunctorBase;
import org.funcito.functorbase.FunctorFactory;
import org.funcito.internal.InvokableState;
import org.funcito.modifier.Modifier;
import org.funcito.modifier.UntypedModifier;

public class JediFilter<T> implements Filter<T> {

    private FunctorBase<T,Boolean> functorBase;

    public JediFilter(InvokableState state, Modifier<T,Boolean> mod) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mod);
    }

    public JediFilter(InvokableState state, UntypedModifier mod) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mod);
    }

    @Override
    public Boolean execute(T from) {
        return functorBase.applyImpl(from);
    }
}
