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
package org.funcito.internal;

import org.funcito.FuncitoException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invokable<T,V> {

    private Method method;
    private Class<T> targetClass;
    private Object[] bindArgs;
    private boolean chainable;

    public Invokable(Method method, Class<T> targetClass, boolean chainable, Object... bindArgs) {
        method.setAccessible(true);
        this.method = method;
        this.targetClass = targetClass;
        this.bindArgs = bindArgs;
        this.chainable = chainable;
    }

    public boolean isChainable() {
        return chainable;
    }

    @SuppressWarnings("unchecked")
    public V invoke(T target) {
        try {
            return (V) method.invoke(target, bindArgs);
        } catch (InvocationTargetException e) {
            throw new FuncitoException("Caught throwable " + e.getCause().getClass().getName() +
                " invoking Funcito Invokable for Method " +
                target.getClass().getName() + "." + getMethodName() + "()", e);
        } catch (Throwable e) {
            if (e instanceof IllegalArgumentException && !targetClass.isInstance(target)) {
                throw new FuncitoException("You attempted to invoke method " +
                        target.getClass().getName() + "." + getMethodName() + "() " +
                        "but defined Funcito invokable was for method " +
                        targetClass.getName() +  "." + getMethodName() + "() ", e);
            }
            throw new FuncitoException("Caught throwable " + e.getClass().getName() +
                    " invoking Funcito Invokable for Method " +
                    target.getClass().getName() + "." + getMethodName() + "()", e);
        }
    }

    public String getMethodName() { return method.getName(); }
}
