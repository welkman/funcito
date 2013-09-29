/*
 * Copyright 2013 Project Funcito Contributors
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

import fj.Effect;
import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;
import org.funcito.functorfactory.FunctorFactory;
import org.funcito.mode.TypedMode;
import org.funcito.mode.Mode;

public class FjEffect<T> extends Effect<T> {

    private FunctorBase<T,Void> functorBase;

    public FjEffect(InvokableState state, TypedMode<T,Void> mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    public FjEffect(InvokableState state, Mode mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    @Override
    public void e(T from) {
        functorBase.applyImpl(from);
    }
}
