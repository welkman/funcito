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
package org.funcito.play;

import org.funcito.functorbase.FunctorBase;
import org.funcito.functorfactory.FunctorFactory;
import org.funcito.internal.InvokableState;
import org.funcito.mode.Mode;
import org.funcito.mode.UntypedMode;
import play.libs.F;

public class Play2Callback<T> implements F.Callback<T> {

    private FunctorBase<T,Void> functorBase;

    public Play2Callback(InvokableState state, Mode<T,Void> mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    public Play2Callback(InvokableState state, UntypedMode mode) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mode);
    }

    @Override
    public void invoke(T from) {
        functorBase.applyImpl(from);
    }
}
