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

import org.funcito.functionaljava.MethodF;
import org.funcito.guava.DefaultableMethodPredicate;
import org.funcito.guava.MethodFunction;
import org.funcito.guava.MethodPredicate;
import org.funcito.internal.stub.StubFactory;

import static org.funcito.internal.WrapperType.*;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fj.F;

public class FuncitoDelegate {

    private final InvocationManager invocationManager = new InvocationManager();
    
    public <T> T stub(Class<T> clazz) {
        return StubFactory.instance().stub(clazz);
    }

    //-------------------- Google Guava -------------------------

    public <T,V> Function<T,V> functionFor(V ignoredRetVal) {
        final Invokable<T,V> invokable = getInvokable(GUAVA_FUNCTION);
        return new MethodFunction<T, V>(invokable);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal) {
        final Invokable<T,Boolean> invokable = getInvokable(GUAVA_PREDICATE);
        return new MethodPredicate<T>(invokable);
    }

    public <T> Predicate<T> predicateFor(Boolean ignoredRetVal, boolean defaultForNull) {
        final Invokable<T,Boolean> invokable = getInvokable(GUAVA_PREDICATE);
        return new DefaultableMethodPredicate<T>(invokable, defaultForNull);
    }
    //-------------------- Functional Java -------------------------

    public <T,V> F<T,V> fFor(V ignoredRetVal) {
        final Invokable<T,V> invokable = getInvokable(FJ_F);
        return new MethodF<T, V>(invokable);
    }

    //-------------------- Funcito Core -------------------------

    public void putInvokable(Invokable invokable) {
        invocationManager.pushInvokable(invokable);
    }
    
    private Invokable getInvokable(WrapperType wrapperType) {
        return invocationManager.extractInvokable(wrapperType.toString());
    }
}
