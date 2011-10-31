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
import org.funcito.Invokable;
import org.funcito.StubInvocation;

import java.lang.reflect.Method;

@GwtIncompatible(value="Depends on CGLib bytecode generation library")
public class CglibInvocation<T,V> implements StubInvocation<T,V> {
    private Object stub;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;

    public CglibInvocation(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        this.stub = o;
        this.method = method;
        this.args = objects;
        this.methodProxy = methodProxy;
    }

    public Invokable<T,V> getInvokable() {
        return new CglibInvokable<T,V>(methodProxy);
    }
}
