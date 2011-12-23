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
     * generates a stub object for use with other API calls
     * @param clazz is the class to be stubbed
     * @return a stub which can be used by other API calls
     */
    public static <T> T callsTo(Class<T> clazz) {
        return fjDelegate.stub(clazz);
    }

    public static <T,V> F<T,V> fFor(V ignoredRetVal) {
        return fjDelegate.fFor(ignoredRetVal);
    }

    static FJDelegate delegate() {
        return fjDelegate;
    }
}
