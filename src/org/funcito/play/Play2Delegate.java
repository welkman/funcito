package org.funcito.play;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import play.libs.F.Callback;
import play.libs.F.Function;

import static org.funcito.internal.WrapperType.PLAY2_FUNCTION;
import static org.funcito.internal.WrapperType.PLAY2_CALLBACK;
import static org.funcito.internal.WrapperType.PLAY2_VOID_CALLBACK;

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
public class Play2Delegate extends FuncitoDelegate {
    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        final InvokableState state = extractInvokableState(PLAY2_FUNCTION);
        return new Play2Function<T, V>(state);
    }

    public <T> Callback<T> callbackFor(Object proxiedMethodCall) {
        InvokableState state = extractInvokableState(PLAY2_CALLBACK);
        return new Play2Callback<T>(state);
    }

    public <T> T prepareVoid(T t) { return t; }

    public <T> Callback<T> voidCallback() {
        InvokableState state = extractInvokableState(PLAY2_VOID_CALLBACK);
        return new Play2Callback<T>(state);
    }

    public <T> Callback<T> voidCallback(Class<T> validationTargetClass) {
        validatePreparedVoidCall(validationTargetClass, PLAY2_VOID_CALLBACK);
        return voidCallback();
    }

}
