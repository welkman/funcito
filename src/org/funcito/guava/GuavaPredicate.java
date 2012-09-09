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

import com.google.common.base.Predicate;

import org.funcito.*;
import org.funcito.internal.InvokableState;

public class GuavaPredicate<T> implements Predicate<T> {

    private NullValidatingPredicateBase<T> functionalBase;

    public GuavaPredicate(InvokableState state) {
        try {
            functionalBase = new NullValidatingPredicateBase<T>(state, Predicate.class,
                    FuncitoGuava.class.getMethod("predicateFor", Boolean.class, boolean.class));
        } catch (NoSuchMethodException e) { // ignored
        }
    }

    @Override
    public boolean apply(T from) {
        return functionalBase.applyImpl(from);
    }

}
