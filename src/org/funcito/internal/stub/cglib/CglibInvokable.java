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

import net.sf.cglib.proxy.MethodProxy;
import org.funcito.FuncitoException;
import org.funcito.internal.Invokable;

public class CglibInvokable<T,V> implements Invokable<T,V> {

    private MethodProxy methodProxy;
    private Class declaringClass;

    public CglibInvokable(MethodProxy methodProxy, Class declaringClass) {
        this.methodProxy = methodProxy; // we keep MethodProxy because it executes faster than Method
        this.declaringClass = declaringClass;
    }

    @SuppressWarnings({"unchecked"})
    public V invoke(T from, Object... args) {
        try {
            return (V)methodProxy.invoke(from, args);
        } catch (Throwable e) {
            if (e instanceof ClassCastException && !declaringClass.isInstance(from)) {
                throw new FuncitoException("You attempted to invoke method " +
                        from.getClass().getName() + "." + getMethodName() + "() " +
                        "but defined Funcito invokable was for method " +
                        declaringClass.getName() +  "." + getMethodName() + "() ", e);
            }
            throw new FuncitoException("Caught throwable " + e.getClass().getName() +
                    " invoking Funcito Invokable for Method " +
                    from.getClass().getName() + "." + getMethodName() + "()", e);
        }
    }

    public int getArgumentsLength() { return methodProxy.getSignature().getArgumentTypes().length; }
    public String getMethodName() { return methodProxy.getSignature().getName(); }
}
