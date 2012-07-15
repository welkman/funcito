/*
 * Copyright 2011 Project Funcito Contributors
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
package org.funcito.internal.stub.javassist;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

import org.funcito.internal.stub.ProxyMethodHandler;

public class JavassistMethodHandler implements MethodHandler {
    private final ProxyMethodHandler proxyMethodHandler = new ProxyMethodHandler();

    public Object invoke(Object proxyTarget, Method method, Method method1, Object[] bindArgs) throws Throwable {
        return proxyMethodHandler.handleProxyMethod(proxyTarget, method, bindArgs);
    }
}
