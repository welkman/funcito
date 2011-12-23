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
 * This class is the entry point of the Funcito API for Jedi.
 */
public class FuncitoJedi {

    private static final JediDelegate jediDelegate = new JediDelegate();

    /**
     * generates a stub object for use with other API calls
     * @param clazz is the class to be stubbed
     * @return a stub which can be used by other API calls
     */
    public static <T> T callsTo(Class<T> clazz) {
        return jediDelegate.stub(clazz);
    }

    public static <T,V> Functor<T, V> functorFor(V stubbedMethodCall) {
        return jediDelegate.functorFor(stubbedMethodCall);
    }

    public static <T> Filter<T> filterFor(Boolean stubbedMethodCall) {
        return jediDelegate.filterFor(stubbedMethodCall);
    }

    static JediDelegate delegate() {
        return delegate();
    }
}
