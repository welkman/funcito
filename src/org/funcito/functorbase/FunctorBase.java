/*
 * Copyright 2013 Project Funcito Contributors
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
package org.funcito.functorbase;

/**
 * The general Funcito functor-interface used internally for every variety of functor operation.
 * @param <T> The target (input) type fo the functor
 * @param <V> The output type of the functor
 */
public interface FunctorBase<T, V> {
    /**
     * The execution method for applying the functor to an input
     * @param from the target (input) value to apply the functor to
     * @return the result of applying the functor
     */
    V applyImpl(T from);
}
