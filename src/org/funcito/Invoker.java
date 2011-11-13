package org.funcito;

/**
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
public class Invoker<T,V> {
    private Invokable<T,V> invokable;

    public Invoker(Invokable<T, V> invokable) {
        this.invokable = invokable;
    }

    public V apply(T from) {
        try {
            return invokable.invoke(from, (Object[])null); //no arguments are passed, because this s/b a no-arg method
        } catch (Throwable e) {
            throw new FuncitoException("error applying Funcito Invokable for Method " +
                    from.getClass().getName() + "." + invokable.getMethodName() + "()", e);
        }
    }

}
