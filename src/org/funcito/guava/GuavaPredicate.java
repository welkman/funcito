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

import org.funcito.FuncitoException;
import org.funcito.FunctionalBase;
import org.funcito.internal.InvokableState;

public class GuavaPredicate<T> implements Predicate<T> {

    private NullValidatingFunctionalBase<T,Boolean> functionalBase;

    class NullValidatingFunctionalBase<T,V> extends FunctionalBase<T,V> {
        NullValidatingFunctionalBase(InvokableState state) {
            super(state);
        }
        @Override
        protected void validateReturnValue(Object retVal) {
            if (retVal==null) {
                throw new FuncitoException("Predicate had a null Boolean return value.\n " +
                    "Guava Predicate expects a non-null Boolean so that it can be autoboxed to a primitive boolean.\n " +
                    "You might consider the alternate method: predicateFor(Boolean proxiedMethodCall, boolean defaultForNull)");
            }
        }
    }

    public GuavaPredicate(InvokableState state) {
        functionalBase = new NullValidatingFunctionalBase<T, Boolean>(state);
    }

    @Override
    public boolean apply(T from) {
        return functionalBase.applyImpl(from);
    }

}
