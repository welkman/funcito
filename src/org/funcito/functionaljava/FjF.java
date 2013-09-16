/*
 * Copyright 2011 Project Funcito Contributors
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.funcito.functionaljava;

import fj.F;

import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;
import org.funcito.functorfactory.FunctorFactory;
import org.funcito.mode.Mode;
import org.funcito.mode.UntypedMode;

public class FjF<T,V> extends F<T,V> {

    private FunctorBase<T,V> functorBase;

    public FjF(InvokableState state, Mode<T,V> mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    public FjF(InvokableState state, UntypedMode mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    @Override
    public V f(T from) {
        return functorBase.applyImpl(from);
    }
}
