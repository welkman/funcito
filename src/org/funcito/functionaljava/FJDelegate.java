package org.funcito.functionaljava;

import fj.Effect;
import fj.F;
import org.funcito.FuncitoException;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;
import org.funcito.internal.WrapperType;

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

    public <T,V> F<T,V> fFor(V ignoredRetVal) {
        InvokableState state = extractInvokableState(FJ_F);
        return new FjF<T, V>(state);
    }

    public <T> Effect<T> effectFor(Object proxiedMethodCall) {
        InvokableState state = extractInvokableState(FJ_EFFECT);
        return new FjEffect<T>(state);
    }

    public <T> T prepareVoid(T t) { return t; }

    public <T> Effect<T> voidEffect() {
        InvokableState state = extractInvokableState(FJ_VOID_EFFECT);
        return new FjEffect<T>(state);
    }

    public <T> Effect<T> voidEffect(Class<T> validationTargetClass) {
        validatePreparedVoidCall(validationTargetClass, WrapperType.FJ_VOID_EFFECT);
        return voidEffect();
    }
}
