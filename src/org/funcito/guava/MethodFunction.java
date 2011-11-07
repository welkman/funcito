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
package org.funcito.guava;

import com.google.common.base.Function;
import org.funcito.FuncitoException;
import org.funcito.Invokable;

public class MethodFunction<T, V> implements Function<T,V> {
    private Invokable<T,V> invokable;

    public MethodFunction(Invokable<T,V> invokable) {
        this.invokable = invokable;
    }

    public V apply(T from) {
        try {
            return invokable.invoke(from, (Object[])null); //no arguments are passed, because this s/b a no-arg method
        } catch (Throwable e) {
            throw new FuncitoException("error trying to invoke Funcito Invokable", e);
        }
    }

}
