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
package org.funcito.internal.stub.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.funcito.internal.stub.ProxyMethodHandler;

public class CglibMethodInterceptor implements MethodInterceptor {
    private final ProxyMethodHandler proxyMethodHandler = new ProxyMethodHandler();

    public Object intercept(Object proxyTarget, Method method, Object[] bindArgs, MethodProxy methodProxy) throws Throwable {
        return proxyMethodHandler.handleProxyMethod(proxyTarget, method, bindArgs);
    }

}
