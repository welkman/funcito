/*
 * Copyright 2011-2013 Project Funcito Contributors
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
package org.funcito.guava;

import com.google.common.base.Predicate;
import org.funcito.internal.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;

// TODO: consider refactor to make this & CollectGenDefaultablePredicate to be a single PrimitivePredicateBase
// TODO: And maybe have a corresponding Modifier (and then deprecate the special method)
public class GuavaDefaultablePredicate<T> implements Predicate<T> {

    private FunctorBase<T,Boolean> functorBase;
    private boolean defaultForNull;

    public GuavaDefaultablePredicate(InvokableState state, boolean defaultForNull) {
        functorBase = new FunctorBase<T, Boolean>(state);
        this.defaultForNull = defaultForNull;
    }

    @Override
    public boolean apply(T from) {
        Boolean retVal = functorBase.applyImpl(from);
        return (retVal==null) ? defaultForNull : retVal;
    }
}
