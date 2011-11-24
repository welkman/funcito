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
package org.funcito.cglib;

import com.google.common.annotations.GwtIncompatible;
import net.sf.cglib.proxy.MethodProxy;
import org.funcito.FuncitoException;
import org.funcito.internal.Invokable;

@GwtIncompatible(value="Depends on CGLib bytecode generation library")
public class CglibInvokable<T,V> implements Invokable<T,V> {

    private MethodProxy methodProxy;

    public CglibInvokable(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    @SuppressWarnings({"unchecked"})
    public V invoke(T from, Object... args) {
        try {
            return (V)methodProxy.invoke(from, args);
        } catch (ClassCastException e) {
            throw new FuncitoException("ClassCastException while invoking Funcito Invokable for Method " +
                    from.getClass().getName() + "." + getMethodName() + "()", e);
        } catch (Throwable e2) {
            throw new FuncitoException("error invoking Funcito Invokable for Method " +
                    from.getClass().getName() + "." + getMethodName() + "()", e2);
        }
    }

    public int getArgumentsLength() { return methodProxy.getSignature().getArgumentTypes().length; }
    public String getMethodName() { return methodProxy.getSignature().getName(); }
}
