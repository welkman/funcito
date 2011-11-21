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
package org.funcito.javassist;

import com.google.common.annotations.GwtIncompatible;
import org.funcito.Invokable;

import java.lang.reflect.Method;

@GwtIncompatible(value = "Depends on Javassist bytecode generation library")
public class JavassistInvokable<T, V> implements Invokable<T, V> {

    private Method method;

    public JavassistInvokable(Method method) {
        method.setAccessible(true);
        this.method = method;
    }

    @SuppressWarnings({"unchecked"})
    public V invoke(T from, Object... args) throws Throwable {
        return (V) method.invoke(from, args);
    }

    public int getArgumentsLength() {
        return method.getParameterTypes().length;
    }

    public String getMethodName() {
        return method.getName();
    }
}
