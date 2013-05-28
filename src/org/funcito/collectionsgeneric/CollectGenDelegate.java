package org.funcito.collectionsgeneric;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.Predicate;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;

import static org.funcito.internal.WrapperType.*;

/*
 * Copyright 2012 Project Funcito Contributors
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
public class CollectGenDelegate extends FuncitoDelegate {
    public <T,V> Transformer<T,V> transformerFor(V ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenDefaultablePredicate<T>(state, defaultForNull);
    }
}
