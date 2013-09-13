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
import org.funcito.modifier.Modifier;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.funcito.modifier.Modifiers;
import org.funcito.modifier.NoOp;
import org.funcito.modifier.UntypedModifier;

import static org.funcito.internal.WrapperType.GUAVA_PREDICATE;
import static org.funcito.internal.WrapperType.GUAVA_FUNCTION;

// TODO: javadoc class
public class GuavaDelegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoGuava.functionFor(V)</code>
     * @see org.funcito.FuncitoGuava#functionFor(Object)
     */
    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        return functionFor(ignoredRetVal, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoGuava.functionFor(V,Modifier)</code>
     * @see org.funcito.FuncitoGuava#functionFor(Object, org.funcito.modifier.Modifier)
     */
    public <T,V> Function<T,V> functionFor(@SuppressWarnings("unused") V ignoredRetVal, Modifier<T,V> mod) {
        final InvokableState state = extractInvokableState(GUAVA_FUNCTION);
        return new GuavaFunction<T, V>(state, mod);
    }

    /**
     * Delegated version of <code>FuncitoGuava.functionFor(V,UntypedModifier)</code>
     * @see org.funcito.FuncitoGuava#functionFor(Object,UntypedModifier)
     */
    public <T,V> Function<T,V> functionFor(@SuppressWarnings("unused") V ignoredRetVal, UntypedModifier mod) {
        final InvokableState state = extractInvokableState(GUAVA_FUNCTION);
        return new GuavaFunction<T, V>(state, mod);
    }

    // TODO: javadoc
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused")Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state);
    }

    // TODO: javadoc
    @Deprecated
    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        return predicateFor(ignoredRetVal, Modifiers.defaultBool(defaultForNull));
    }

    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, Modifier<T,Boolean> mod) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state, mod);
    }

    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, UntypedModifier mod) {
        final InvokableState state = extractInvokableState(GUAVA_PREDICATE);
        return new GuavaPredicate<T>(state, mod);
    }


}
