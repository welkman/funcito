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
package org.funcito.rxjava;

import org.funcito.FunctionalBase;
import org.funcito.internal.InvokableState;
import rx.util.functions.Func1;

public class RxJavaFunc1<T, V> implements Func1<T,V> {

    private FunctionalBase<T,V> functionalBase;

    public RxJavaFunc1(InvokableState state) {
        functionalBase = new FunctionalBase<T, V>(state);
    }

    @Override
    public V call(T from) {
        return functionalBase.applyImpl(from);
    }
}
