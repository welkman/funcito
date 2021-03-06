package org.funcito.functionaljava;

import fj.Effect;
import fj.F;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.funcito.mode.TypedMode;
import org.funcito.mode.NoOp;
import org.funcito.mode.Mode;

import static org.funcito.internal.WrapperType.FJ_EFFECT;
import static org.funcito.internal.WrapperType.FJ_F;
import static org.funcito.internal.WrapperType.FJ_VOID_EFFECT;

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
public class FJDelegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoFJ.fFor(V)</code>
     * @see org.funcito.FuncitoFJ#fFor(Object)
     */
    public <T,V> F<T,V> fFor(V ignoredRetVal) {
        InvokableState state = extractInvokableState(FJ_F);
        return new FjF<T, V>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoFJ.functionFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoFJ#fFor(Object, org.funcito.mode.TypedMode)
     */
    public <T,V> F<T,V> fFor(V ignoredRetVal, TypedMode<V> mode) {
        final InvokableState state = extractInvokableState(FJ_F);
        return new FjF<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoFJ.fFor(V,Mode)</code>
     * @see org.funcito.FuncitoFJ#fFor(Object, org.funcito.mode.Mode)
     */
    public <T,V> F<T,V> fFor(V ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(FJ_F);
        return new FjF<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoFJ.effectFor(Object)</code>
     * @see org.funcito.FuncitoFJ#effectFor(Object)
     */
    public <T> Effect<T> effectFor(Object proxiedMethodCall) {
        InvokableState state = extractInvokableState(FJ_EFFECT);
        return new FjEffect<T>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoFJ.effectFor(Object,TypedMode)</code>
     * @see org.funcito.FuncitoFJ#effectFor(Object,TypedMode)
     */
    public <T> Effect<T> effectFor(Object proxiedMethodCall, TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(FJ_EFFECT);
        return new FjEffect<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoFJ.effectFor(Object,Mode)</code>
     * @see org.funcito.FuncitoFJ#effectFor(Object,Mode)
     */
    public <T> Effect<T> effectFor(Object proxiedMethodCall, Mode mode) {
        InvokableState state = extractInvokableState(FJ_EFFECT);
        return new FjEffect<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoFJ.prepareVoid(T)</code>
     * @see org.funcito.FuncitoFJ#prepareVoid(Object)
     */
    public <T> T prepareVoid(T t) { return t; }

    /**
     * Delegated version of <code>FuncitoFJ.voidEffect()</code>
     * @see org.funcito.FuncitoFJ#voidEffect()
     */
    public <T> Effect<T> voidEffect() {
        InvokableState state = extractInvokableState(FJ_VOID_EFFECT);
        return new FjEffect<T>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoFJ.voidEffect(TypedMode)</code>
     * @see org.funcito.FuncitoFJ#voidEffect(TypedMode)
     */
    public <T> Effect<T> voidEffect(TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(FJ_VOID_EFFECT);
        return new FjEffect<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoFJ.voidEffect(Mode)</code>
     * @see org.funcito.FuncitoFJ#voidEffect(Mode)
     */
    public <T> Effect<T> voidEffect(Mode mode) {
        InvokableState state = extractInvokableState(FJ_VOID_EFFECT);
        return new FjEffect<T>(state, mode);
    }
}
