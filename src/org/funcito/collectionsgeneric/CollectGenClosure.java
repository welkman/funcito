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
package org.funcito.collectionsgeneric;

import org.apache.commons.collections15.Closure;
import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;
import org.funcito.functorbase.FunctorFactory;
import org.funcito.modifier.Modifier;
import org.funcito.modifier.UntypedModifier;

public class CollectGenClosure<T> implements Closure<T> {

    private FunctorBase<T,Void> functorBase;

    public CollectGenClosure(InvokableState state, Modifier<T,Void> mod) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mod);
    }

    public CollectGenClosure(InvokableState state, UntypedModifier mod) {
        functorBase = FunctorFactory.instance().makeFunctionalBase(state, mod);
    }

    @Override
    public void execute(T from) {
        functorBase.applyImpl(from);
    }
}
