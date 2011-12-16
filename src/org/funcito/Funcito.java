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
package org.funcito;

import org.funcito.functionaljava.FJDelegate;
import org.funcito.guava.GuavaDelegate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import fj.F;

public class Funcito {

    public static class Guava {

        private static final GuavaDelegate guavaDelegate = new GuavaDelegate();

        public static <T> T callsTo(Class<T> clazz) {
            return guavaDelegate.stub(clazz);
        }

        public static <T,V> Function<T,V> functionFor(V stubbedMethodCall) {
            return guavaDelegate.functionFor(stubbedMethodCall);
        }

        public static <T> Predicate<T> predicateFor(Boolean stubbedMethodCall) {
            return guavaDelegate.predicateFor(stubbedMethodCall);
        }

        public static <T> Predicate<T> predicateFor(Boolean stubbedMethodCall, boolean defaultForNull) {
            return guavaDelegate.predicateFor(stubbedMethodCall, defaultForNull);
        }
    }

    public static class Fj {
        private static final FJDelegate fjDelegate = new FJDelegate();

        public static <T> T callsTo(Class<T> clazz) {
            return fjDelegate.stub(clazz);
        }

        public static <T,V> F<T,V> fFor(V ignoredRetVal) {
            return fjDelegate.fFor(ignoredRetVal);
        }
    }

}
