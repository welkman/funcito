/*
 * Copyright 2011 Project Funcito Contributors
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
import org.funcito.internal.Invokable;

public class MethodFunctor<T, V> implements Functor<T,V> {
    private Invokable<T,V> invokable;

    public MethodFunctor(Invokable<T, V> invokable) {
        this.invokable = invokable;
    }

    public V execute(T from) {
        return invokable.invoke(from);
    }
}
