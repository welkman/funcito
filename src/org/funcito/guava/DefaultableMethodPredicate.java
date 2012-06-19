package org.funcito.guava;

import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;

import java.util.Iterator;

/*
 * Copyright 2011 Project Funcito Contributors
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DefaultableMethodPredicate<T> extends MethodPredicate<T> {
    private boolean defaultForNull;

    public DefaultableMethodPredicate(InvokableState state, boolean defaultForNull) {
        super(state);
        this.defaultForNull = defaultForNull;
    }

    public boolean apply(T from) {
        Object retVal = firstInvokable.invoke(from);
        if (unchained) {
            return (retVal==null) ? defaultForNull : (Boolean)retVal;
        }
        Iterator<Invokable> iter = state.iterator();
        iter.next(); // skip the head which has already been processed
        while (iter.hasNext()) {
            retVal = iter.next().invoke(retVal);
        }
        return (retVal==null) ? defaultForNull : (Boolean)retVal;
    }
}
