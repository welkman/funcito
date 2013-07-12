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

/**
 * Note this class is immutable
 * @param <T>
 * @param <V>
 */
public class Invokable<T,V> {

    private Method method;
    private T target;
    private V retVal;
    private Class<T> targetClass;
    private Object[] bindArgs;

    public Invokable(Method method, T target, V retVal, Object... bindArgs) {
        method.setAccessible(true);
        this.method = method;
        this.target = target;
        this.retVal = retVal;
        this.targetClass = (Class<T>)target.getClass();
        this.bindArgs = bindArgs;
    }

    public Object getTarget() { return this.target; }
    public Object getRetVal() { return this.retVal; }

    @SuppressWarnings("unchecked")
    public V invoke(T target) {
        try {
            return (V) method.invoke(target, bindArgs);
        } catch (InvocationTargetException e) {
            throw new FuncitoException("Caught throwable " + e.getCause().getClass().getName() +
                    " \n" +
                    "while invoking Funcito Invokable for method " + getMethodName(), e);
        } catch (Throwable e) {
            if (e instanceof IllegalArgumentException && !targetClass.isInstance(target)) {
                throw new FuncitoException("You attempted to invoke method " + getInvokedMethodName(target)+
                        " \n" +
                        "but defined Funcito invokable was for method " + getMethodName() +
                        ". \n" +
                        "Probable Cause: identical method signatures not inherited from common base class", e);
            }
            throw new FuncitoException("Caught throwable " + e.getClass().getName() +
                    " \n" +
                    "while invoking Funcito Invokable for method " + getMethodName(), e);
        }
    }

    private String getInvokedMethodName(Object invokedTarget) {
        Method m = null;
        try {
            m = invokedTarget.getClass().getMethod(method.getName());
        } catch (NoSuchMethodException e) {
            throw new FuncitoException("Method " + method.getName() + "() does not exist in invoked target class " + invokedTarget.getClass().getName() +
                    " \n" +
                    "Probable cause: wrapping void methods with voidXXXX() generator methods calls, can result in unsafe assignments.  To guarantee safe assignment," +
                    "see the type-safe assignment overloaded form: voidXXXX(Class target");
        }
        return m.toGenericString();
    }

    public String getMethodName() { return method.toGenericString(); }
}
