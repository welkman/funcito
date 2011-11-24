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

import org.funcito.StubFactory;
import org.funcito.functionaljava.MethodF;
import org.funcito.guava.MethodFunction;
import org.funcito.guava.MethodPredicate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fj.F;

public class FuncitoDelegate {
    private static final InvocationManager invocationManager = new InvocationManager();

    public static InvocationManager getInvocationManager() { return invocationManager; }

    public <T> T stub(Class<T> clazz) {
        return StubFactory.instance().stub(clazz);
    }

    /**
     * Alias for "fluent" syntax
     */
    public <T> T callsTo(Class<T> clazz) {
        return stub(clazz);
    }

    //--------------------        static calls for Google Guava      -------------------------

    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        final Invokable<T,V> invokable = invocationManager.extractInvokable("Guava Function");
        return new MethodFunction<T, V>(invokable);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal) {
        final Invokable<T,Boolean> invokable = invocationManager.extractInvokable("Guava Predicate");
        return new MethodPredicate<T>(invokable);
    }

    //--------------------        static calls for Functional Java    -------------------------

    public <T,V> F<T,V> fFor(V ignoredRetVal) {
        final Invokable<T,V> invokable = invocationManager.extractInvokable("Functional Java F (function)");
        return new MethodF<T, V>(invokable);
    }

}
