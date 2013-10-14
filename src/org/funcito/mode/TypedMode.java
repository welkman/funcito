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

package org.funcito.mode;

import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.InvokableState;

/**
 * The execution of every functor object created by a Funcito factory method is guided by an instance of either a standard
 * <code>Mode</code> or its sibling <code>TypedMode</code> (this interface).  <code>TypedMode</code> is only preferred
 * in cases where a mode requires semantic knowledge of a return type of the associated functor (generic type <code>V</code>).
 */
public interface TypedMode<V> {
    FunctorBase<?,V> makeBase(InvokableState invokableState);
}
