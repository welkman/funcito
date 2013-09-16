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
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V,Mode)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object, org.funcito.mode.Mode)
     */
    public <T,V> Transformer<T,V> transformerFor(@SuppressWarnings("unused") V ignoredRetVal, Mode<T,V> mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V,UntypedMode)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object, org.funcito.mode.UntypedMode)
     */
    public <T,V> Transformer<T,V> transformerFor(@SuppressWarnings("unused") V ignoredRetVal, UntypedMode mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, mode);
    }

    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state);
    }

    @Deprecated
    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        return predicateFor(ignoredRetVal, Modes.defaultBool(defaultForNull));
    }

    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, Mode<T,Boolean> mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state, mode);
    }

    public <T> Predicate<T> predicateFor(@SuppressWarnings("unused") Boolean ignoredRetVal, UntypedMode mode) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state, mode);
    }

    public <T> Closure<T> closureFor(Object proxiedMethodCall) {
        return closureFor(proxiedMethodCall, NoOp.NO_OP);
    }

    public <T> Closure<T> closureFor(@SuppressWarnings("unused") Object proxiedMethodCall, Mode<T,Void> mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }

    public <T> Closure<T> closureFor(@SuppressWarnings("unused") Object proxiedMethodCall, UntypedMode mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }

    public <T> T prepareVoid(T t) { return t; }

    public <T> Closure<T> voidClosure() {
        return voidClosure(NoOp.NO_OP);
    }

    public <T> Closure<T> voidClosure(Mode<T,Void> mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_VOID_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }

    public <T> Closure<T> voidClosure(UntypedMode mode) {
        InvokableState state = extractInvokableState(COLLECTGEN_VOID_CLOSURE);
        return new CollectGenClosure<T>(state, mode);
    }
}
