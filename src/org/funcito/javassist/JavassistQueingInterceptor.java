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
import javassist.util.proxy.MethodHandler;
import org.funcito.Funcito;
import org.funcito.google.guava.common.base.Defaults;

import java.lang.reflect.Method;

@GwtIncompatible(value = "Depends on Javassist bytecode generation library")
public class JavassistQueingInterceptor implements MethodHandler {

    public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
        Funcito.getInvocationManager().pushInvokable(new JavassistInvokable(method));
        return Defaults.defaultValue(method.getReturnType());
    }
}
