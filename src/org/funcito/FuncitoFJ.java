package org.funcito;

import fj.F;
import org.funcito.functionaljava.FJDelegate;

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
 * This class is the entry point of the Funcito API for Functional Java.
 */
public class FuncitoFJ {
    private static final FJDelegate fjDelegate = new FJDelegate();

    /**
     * Generates a stub object for use with other FuncitoFJ static calls.  This callsTo should not
     * be used for any other purposes.  An example of proper usage is as follows:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);
     *     <br>
     *     F<MyClass,RetType> func = fFor( callsTo.noArgMethodWithRetType() );
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a
     * single stub can also be reused for creating multiple Functional Java F (function) objects.
     * @param clazz is the class to be stubbed
     * @return a stub which can be used by other FuncitoFJ static calls
     */
    public static <T> T callsTo(Class<T> clazz) {
        return fjDelegate.callsTo(clazz);
    }

    /**
     * Generates a Functional Java F (function) object that wraps a method call.  Resulting F is as thread-safe as the method itself.
     * @param stubbedMethodCall is the return value from a method call to a FuncitoFJ stub object
     * @return a Functional Java F (function) object that wraps the method call.
     */
    public static <T,V> F<T,V> fFor(V stubbedMethodCall) {
        return fjDelegate.fFor(stubbedMethodCall);
    }

    static FJDelegate delegate() {
        return fjDelegate;
    }
}
