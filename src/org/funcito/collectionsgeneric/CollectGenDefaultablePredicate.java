/*
 * Copyright 2012-2013 Project Funcito Contributors
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
package org.funcito.collectionsgeneric;

import org.funcito.internal.InvokableState;
import org.funcito.modifier.PrimitiveBoolDefault;

// TODO: document the deprecation, also add independent unit tests
@Deprecated
public class CollectGenDefaultablePredicate<T> extends CollectGenPredicate<T> {

    public CollectGenDefaultablePredicate(InvokableState state, boolean defaultForNull) {
        super(state, new PrimitiveBoolDefault<T>(defaultForNull));
    }
}
