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

import org.funcito.internal.stub.utils.Defaults;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class ProxyMethodHandler {

    public Object handleProxyMethod(Object proxyTarget, Method method, Object[] bindArgs) {
        Class<?> returnType = method.getReturnType();
        // Try to generate a default Primitive-wrapper return value for primitive return types
        Object retVal = Defaults.defaultValue(returnType);
        // if not primitive see if return type is candidate for proxying, to support chaining
        if (retVal == null && !returnTypeIsGenericTypeVariable(method)) {
            ProxyFactory factory = ProxyFactory.instance();
            if (factory.canImposterise(returnType)) {
                retVal = factory.proxy(returnType);
            }
        }
        Invokable<?,?> invokable = new Invokable<Object,Object>(method, proxyTarget, retVal, bindArgs);
        new FuncitoDelegate().putInvokable(invokable);
        return retVal;
    }

    // GenericTypeVariable return types (i.e. returns <T>) indicate that at runtime, type-erasure would
    // cause proxying of the return type, resulting in a proxy of Object.class.  That causes an assignment
    // typecast error in the wrapping-code for non-chained methods, AND for chained methods it causes a method
    // not found error.  So we cut our losses by not trying to support this kind of return type proxying and
    // instead default to null.  Now non-chained calls will continue to work.  But chained method calls with
    // GenericTypeVariable return types will generate NPE.
    private boolean returnTypeIsGenericTypeVariable(Method method) {
        return (method.getGenericReturnType() instanceof TypeVariable);
    }
}