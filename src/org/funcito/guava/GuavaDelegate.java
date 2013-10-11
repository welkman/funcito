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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.funcito.mode.*;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;

import static org.funcito.internal.WrapperType.GUAVA_PREDICATE;
import static org.funcito.internal.WrapperType.GUAVA_FUNCTION;

/**
 *
 */
public class GuavaDelegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoGuava.functionFor(V)</code>
     * @see org.funcito.FuncitoGuava#functionFor(Object)
     */
    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        return functionFor(ignoredRetVal, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoGuava.functionFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoGuava#functionFor(Object, TypedMode)
     */
    public <T,V> Function<T,V> functionFor(@SuppressWarnings("unused") V ignoredRetVal, TypedMode<V> mode) {
        final InvokableState state = extractInvokableState(GUAVA_FUNCTION);
        return new GuavaFunction<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoGuava.functionFor(V,Mode)</code>
     * @see org.funcito.FuncitoGuava#functionFor(Object, Mode)
     */
    public <T,V> Function<T,V> functionFor(@SuppressWarnings("unused") V ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(GUAVA_FUNCTION);
        return new GuavaFunction<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoGuava.predicateFor(Boolean)</code>
     * @see org.funcito.FuncitoGuava#predicateFor(Boolean)
     */
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused")Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state);
    }

    /**
     * Delegated version of <code>FuncitoGuava.predicateFor(Boolean, boolean)</code>
     * @see org.funcito.FuncitoGuava#predicateFor(Boolean,boolean)
     * @deprecated It is recommended that <code>FuncitoGuava.predicateFor(Boolean, TypedMode<Boolean>)</code> be used instead with <code>Modes.tailDefault(Boolean)</code> or <code>Modes.safeNav(Boolean)</code>
     */
    @Deprecated
    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        return predicateFor(ignoredRetVal, (TypedMode<Boolean>)Modes.tailDefault(defaultForNull));
    }

    /**
     * Delegated version of <code>FuncitoGuava.predicateFor(Boolean,TypedMode)</code>
     * @see org.funcito.FuncitoGuava#predicateFor(Boolean,TypedMode)
     */
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, TypedMode<Boolean> mode) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoGuava.predicateFor(Boolean,Mode)</code>
     * @see org.funcito.FuncitoGuava#predicateFor(Boolean,Mode)
     */
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state, mode);
    }
}
