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

import org.funcito.internal.InvokableState;
import org.funcito.functorbase.FunctorBase;

/**
 * The execution of every functor object created by a Funcito factory method is guided by an instance of either a standard
 * <code>Mode</code> (this interface) or its cousin <code>TypedMode</code>.  Where no <code>Mode</code> or
 * <code>TypedMode</code> is provided to a factory method, the <code>NoOp</code> mode is used.
 * <p/>
 * The purpose of the method <code>makeBase</code> that implementors are required to write is to take the captured
 * <code>InvokableState</code> (a method call, or chain of method calls), and return a <code>FunctorBase</code> that
 * defines how that InvokableState is to be executed when a wrapping 3rd party API function-like object is applied.  A
 * basic example implementation of a Mode is found in <code>NoOp</code>.
 *
 *@see TypedMode
 *@see NoOp
 */
public interface Mode {
    /**
     * Converts a captured invokable state into a FunctorBase that guides the execution of a wrapping 3rd party API
     * function-like object.
     * @param invokableState is the extracted method or method chain that is to be wrapped
     * @return an untyped FunctorBase object that will be used to execute the captured method or method-chain.
     */
    FunctorBase<?,?> makeBase(InvokableState invokableState);
}
