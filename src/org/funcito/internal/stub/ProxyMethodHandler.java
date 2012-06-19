/*
 * Copyright 2012 Project Funcito Contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.funcito.internal.stub;

import org.funcito.google.guava.common.base.Defaults;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;

import java.lang.reflect.Method;

public class ProxyMethodHandler {
    public ProxyMethodHandler() {
    }

    public Object handleProxyMethod(Object proxyTarget, Method method, Object[] bindArgs) {
        boolean chainable = false;
        Object retVal = Defaults.defaultValue(method.getReturnType());
        if (retVal == null) {
            StubFactory factory = StubFactory.instance();
            if (factory.canImposterise(method.getReturnType())) {
                retVal = factory.stub(method.getReturnType(), ProxyReturnValue.class);
                chainable = true;
            }
        }
        Invokable invokable = new Invokable(method, proxyTarget.getClass(), chainable, bindArgs);
        new FuncitoDelegate().putInvokable(invokable);
        return retVal;
    }
}