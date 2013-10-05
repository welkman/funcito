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

import org.apache.commons.collections15.Closure;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.funcito.mode.*;

import static org.funcito.internal.WrapperType.*;

public class CollectGenDelegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object)
     */
    public <T,V> Transformer<T,V> transformerFor(@SuppressWarnings("unused") V ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object, TypedMode)
     */
    public <T,V> Transformer<T,V> transformerFor(@SuppressWarnings("unused") V ignoredRetVal, TypedMode<V> mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V,Mode)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object, Mode)
     */
    public <T,V> Transformer<T,V> transformerFor(@SuppressWarnings("unused") V ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.predicateFor(V)</code>
     * @see org.funcito.FuncitoCollectGen#predicateFor(Boolean)
     */
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.predicateFor(Boolean, boolean)</code>
     * @see org.funcito.FuncitoCollectGen#predicateFor(Boolean,boolean)
     * @deprecated It is recommended that <code>FuncitoCollectGen.predicateFor(Boolean, TypedMode<Boolean>)</code> be used instead with <code>Modes.tailDefault(Boolean)</code> or <code>Modes.safeNav(Boolean)</code>
     */
    @Deprecated
    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        return predicateFor(ignoredRetVal, (TypedMode<Boolean>)Modes.tailDefault(defaultForNull));
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.predicateFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoCollectGen#predicateFor(Boolean, TypedMode)
     */
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, TypedMode<Boolean> mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.predicateFor(V,Mode)</code>
     * @see org.funcito.FuncitoCollectGen#predicateFor(Boolean, Mode)
     */
    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state, mode);
    }

    public <T> Closure<T> closureFor(Object proxiedMethodCall) {
        return closureFor(proxiedMethodCall, NoOp.NO_OP);
    }

    public <T> Closure<T> closureFor(@SuppressWarnings("unused") Object proxiedMethodCall, TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }

    public <T> Closure<T> closureFor(@SuppressWarnings("unused") Object proxiedMethodCall, Mode mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }

    public <T> T prepareVoid(T t) { return t; }

    public <T> Closure<T> voidClosure() {
        return voidClosure(NoOp.NO_OP);
    }

    public <T> Closure<T> voidClosure(TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_VOID_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }

    public <T> Closure<T> voidClosure(Mode mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_VOID_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }
}
