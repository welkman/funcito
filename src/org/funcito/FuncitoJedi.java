package org.funcito;

import jedi.functional.Filter;
import jedi.functional.Functor;
import org.funcito.jedi.JediDelegate;

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

/**
 * This class is the static entry point of the Funcito API for Jedi.
 */
public class FuncitoJedi {

    private static final JediDelegate jediDelegate = new JediDelegate();

    private FuncitoJedi() {}

    /**
     * Generates a stub object for use with other FuncitoJedi static calls.  This stub should not
     * be used for any other purposes.  An example of proper usage is as follows:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Functor<MyClass,RetType> func = functorFor( callsTo.noArgMethodWithRetType() );
     * </code>
     * <p>
     * Stubs are cached, this method can be called multiple times for the same class without penalty.  But a single stub
     * can also be reused for creating multiple Jedi Functor or Filter objects.
     * @param clazz is the class to be stubbed
     * @return a stub which can be used by other FuncitoJedi static calls
     */
    public static <T> T callsTo(Class<T> clazz) {
        return jediDelegate.callsTo(clazz);
    }

    /**
     * Generates a Jedi Functor object that wraps a method call.  Resulting Functor is as thread-safe as the method itself.
     * @param stubbedMethodCall is the return value from a method call to a FuncitoJedi callsTo object
     * @return a Jedi Functor object that wraps the method call.
     */
    public static <T,V> Functor<T, V> functorFor(V stubbedMethodCall) {
        return jediDelegate.functorFor(stubbedMethodCall);
    }

    /**
     * Generates a Jedi Filter object that wraps a boolean-return method call.  Resulting Filter is as thread-safe as the method itself.
     * Methods returning either boolean primitive or Boolean wrapper objects may be wrapped without problems.
     * @param stubbedMethodCall is the return value from a method call to a FuncitoJedi stub object
     * @return a Jedi Filter object that wraps the method call.
     */
    public static <T> Filter<T> filterFor(Boolean stubbedMethodCall) {
        Filter<T> tFilter = jediDelegate.filterFor(stubbedMethodCall);
        return tFilter;
    }

    public static JediDelegate delegate() {
        return jediDelegate;
    }
}
