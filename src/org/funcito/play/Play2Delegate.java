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
package org.funcito.play;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.funcito.mode.Mode;
import org.funcito.mode.TypedMode;
import org.funcito.mode.NoOp;
import play.libs.F.Callback;
import play.libs.F.Function;

import static org.funcito.internal.WrapperType.PLAY2_FUNCTION;
import static org.funcito.internal.WrapperType.PLAY2_CALLBACK;
import static org.funcito.internal.WrapperType.PLAY2_VOID_CALLBACK;

public class Play2Delegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoPlay2.functionFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoPlay2#functionFor(Object)
     */
    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        final InvokableState state = extractInvokableState(PLAY2_FUNCTION);
        return new Play2Function<T, V>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.functionFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoPlay2#functionFor(Object, TypedMode)
     */
    public <T,V> Function<T,V> functionFor(V ignoredRetVal, TypedMode<V> mode) {
        final InvokableState state = extractInvokableState(PLAY2_FUNCTION);
        return new Play2Function<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.functionFor(V,Mode)</code>
     * @see org.funcito.FuncitoPlay2#functionFor(Object, Mode)
     */
    public <T,V> Function<T,V> functionFor(V ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(PLAY2_FUNCTION);
        return new Play2Function<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.callbackFor(Object,TypedMode)</code>
     * @see org.funcito.FuncitoPlay2#callbackFor(Object)
     */
    public <T> Callback<T> callbackFor(Object proxiedMethodCall) {
        InvokableState state = extractInvokableState(PLAY2_CALLBACK);
        return new Play2Callback<T>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.callbackFor(Object,TypedMode)</code>
     * @see org.funcito.FuncitoPlay2#callbackFor(Object,TypedMode)
     */
    public <T> Callback<T> callbackFor(Object proxiedMethodCall, TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(PLAY2_CALLBACK);
        return new Play2Callback<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.callbackFor(Object,Mode)</code>
     * @see org.funcito.FuncitoPlay2#callbackFor(Object,TypedMode)
     */
    public <T> Callback<T> callbackFor(Object proxiedMethodCall, Mode mode) {
        InvokableState state = extractInvokableState(PLAY2_CALLBACK);
        return new Play2Callback<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.prepareVoid(T)</code>
     * @see org.funcito.FuncitoPlay2#prepareVoid(T)
     */
    public <T> T prepareVoid(T t) { return t; }

    /**
     * Delegated version of <code>FuncitoPlay2.voidCallback()</code>
     * @see org.funcito.FuncitoPlay2#voidCallback()
     */
    public <T> Callback<T> voidCallback() {
        InvokableState state = extractInvokableState(PLAY2_VOID_CALLBACK);
        return new Play2Callback<T>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.voidCallback(TypedMode)</code>
     * @see org.funcito.FuncitoPlay2#voidCallback(TypedMode)
     */
    public <T> Callback<T> voidCallback(TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(PLAY2_VOID_CALLBACK);
        return new Play2Callback<T>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoPlay2.voidCallback(Mode)</code>
     * @see org.funcito.FuncitoPlay2#voidCallback(Mode)
     */
    public <T> Callback<T> voidCallback(Mode mode) {
        InvokableState state = extractInvokableState(PLAY2_VOID_CALLBACK);
        return new Play2Callback<T>(state, mode);
    }
}
