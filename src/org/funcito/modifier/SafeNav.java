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
package org.funcito.modifier;

import org.funcito.functorbase.FunctorBase;
import org.funcito.functorbase.SafeNavFunctor;
import org.funcito.internal.InvokableState;

// TODO: Javadoc
public class SafeNav<T,V> implements Modifier {
    private V nullNavDefault;

    public SafeNav(V nullNavDefault) {
        this.nullNavDefault = nullNavDefault;
    }

    @Override
    public FunctorBase<T,V> makeBase(InvokableState invokableState) {
        return new SafeNavFunctor<T,V>(invokableState, nullNavDefault);
    }
}
