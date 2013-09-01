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
import org.funcito.modifier.Modifier;
import org.funcito.modifier.NoOp;
import org.funcito.modifier.UntypedModifier;

import static org.funcito.internal.WrapperType.*;

public class CollectGenDelegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object)
     */
    public <T,V> Transformer<T,V> transformerFor(V ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V,Modifier)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object, org.funcito.modifier.Modifier)
     */
    public <T,V> Transformer<T,V> transformerFor(V ignoredRetVal, Modifier<T,V> mod) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, mod);
    }

    /**
     * Delegated version of <code>FuncitoCollectGen.transformerFor(V,UntypedModifier)</code>
     * @see org.funcito.FuncitoCollectGen#transformerFor(Object, org.funcito.modifier.UntypedModifier)
     */
    public <T,V> Transformer<T,V> transformerFor(V ignoredRetVal, UntypedModifier mod) {
        final InvokableState state = extractInvokableState(COLLECTGEN_TRANSFORMER);
        return new CollectGenTransformer<T, V>(state, mod);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenPredicate<T>(state);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        final InvokableState state = extractInvokableState(COLLECTGEN_PREDICATE);
        return new CollectGenDefaultablePredicate<T>(state, defaultForNull);
    }

    public <T> Closure<T> closureFor(Object proxiedMethodCall) {
        InvokableState state = extractInvokableState(COLLECTGEN_CLOSURE);
        return new CollectGenClosure<T>(state);
    }

    public <T> T prepareVoid(T t) { return t; }

    public <T> Closure<T> voidClosure() {
        InvokableState state = extractInvokableState(COLLECTGEN_VOID_CLOSURE);
        return new CollectGenClosure<T>(state);
    }

    public <T> Closure<T> voidClosure(Class<T> validationTargetClass) {
        validatePreparedVoidCall(validationTargetClass, COLLECTGEN_VOID_CLOSURE);
        return voidClosure();
    }

}
