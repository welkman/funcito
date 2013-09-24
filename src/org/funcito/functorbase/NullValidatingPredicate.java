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
package org.funcito.functorbase;

import org.funcito.FuncitoException;
import org.funcito.internal.InvokableState;

import java.lang.reflect.Method;

public class NullValidatingPredicate extends BasicFunctor<Object,Boolean> {
    private String apiPredicateClass;
    private String altMethod;

    public NullValidatingPredicate(InvokableState state, Class<?> apiPredicateClass, Method altMethod) {
        super(state);
        this.apiPredicateClass = apiPredicateClass.getName();
        this.altMethod = altMethod.toGenericString();
    }

    @Override
    protected void validateReturnValue(Object retVal) {
        if (retVal==null) {
            throw new FuncitoException(apiPredicateClass + " returned a null Boolean return value.\n " +
                apiPredicateClass + " expects a non-null Boolean so that it can be autoboxed to a primitive boolean.\n " +
                "You might consider the alternate method: " + altMethod);
        }
    }
}
