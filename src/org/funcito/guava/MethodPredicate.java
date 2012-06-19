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
package org.funcito.guava;

import com.google.common.base.Predicate;

import org.funcito.FuncitoException;
import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;

import java.util.Iterator;

public class MethodPredicate<T> implements Predicate<T> {
    final protected InvokableState state;
    final protected Invokable<T,Boolean> firstInvokable;
    final protected boolean unchained;

    public MethodPredicate(InvokableState state) {
        this.state = state;
        Iterator<Invokable> iter = state.iterator();
        this.firstInvokable = iter.next(); // for efficiency for unchained invocations, extract ahead of time
        this.unchained = !iter.hasNext();
    }

    public boolean apply(T from) {
        Object retVal = firstInvokable.invoke(from);
        if (unchained) {
            checkNullBoolean(retVal);
            return (Boolean)retVal;
        }
        Iterator<Invokable> iter = state.iterator();
        iter.next(); // skip the head which has already been processed
        while (iter.hasNext()) {
            retVal = iter.next().invoke(retVal);
        }
        checkNullBoolean(retVal);
        return (Boolean)retVal;
    }

    private void checkNullBoolean(Object retVal) {
        if (retVal==null) {
            throw new FuncitoException("Predicate had a null Boolean return value; " +
                    "Guava Predicates expect a non-null Boolean so that it can be autoboxed to a primitive boolean.\n" +
                    "You might consider the alternate method: predicateFor(Boolean stubbedMethodCall, boolean defaultForNull)");
        }
    }
}
