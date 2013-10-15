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
package org.funcito.mode;

import org.funcito.functorbase.FunctorBase;
import org.funcito.functorbase.NullValidatingPredicate;
import org.funcito.internal.InvokableState;

import java.lang.reflect.Method;

/**
 * This mode is internally used by Funcito for 3rd party APIs with primitive <code>boolean</code>-returning predicates,
 * returning a <code>NullValidatingPredicate</code> that will help diagnose execution problems rather than simply throwing
 * NullPointerException deep in the Funcito internals.  It is unlikely that Funcito users will explicitly use this mode
 * but rather only implicitly through special predicate factory methods.
 * @see org.funcito.FuncitoGuava#predicateFor(Boolean)
 * @see org.funcito.FuncitoCollectGen#predicateFor(Boolean)
 */
public class ValidateNullBoolean implements Mode {
    private Class<?> apiPredicateClass;
    private Method altMethod;

    /**
     * @param apiPredicateClass 3rd party predicate class that is using this, provided here to make diagnostic failure messages more meaningful.
     * @param altMethod recommended alternative factory method, provided here to also enhance diagnostic falure messages
     */
    public ValidateNullBoolean(Class<?> apiPredicateClass, Method altMethod) {
        this.apiPredicateClass = apiPredicateClass;
        this.altMethod = altMethod;
    }

    @Override
    public FunctorBase<?, Boolean> makeBase(InvokableState invokableState) {
        return new NullValidatingPredicate(invokableState, apiPredicateClass, altMethod);
    }
}
