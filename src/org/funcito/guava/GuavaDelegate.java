package org.funcito.guava;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;

import static org.funcito.internal.WrapperType.GUAVA_PREDICATE;
import static org.funcito.internal.WrapperType.GUAVA_FUNCTION;

/*
 * Copyright 2011 Project Funcito Contributors
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
public class GuavaDelegate extends FuncitoDelegate {
    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        final InvokableState state = extractInvokableState(GUAVA_FUNCTION);
        return new GuavaFunction<T, V>(state);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaDefaultablePredicate<T>(state, defaultForNull);
    }
}
