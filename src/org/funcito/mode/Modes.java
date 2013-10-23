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

/**
 * A convenience class of static methods for creating/providing Modes and TypedModes.  Use these modes in the functor
 * factory methods to provide customized behaviors for the resulting functors.  Example usages are:
 * <p>
 * <pre>
 * <code>
 *     final MyClass MY_CLASS = callsTo(MyClass.class);
 *
 *     // The following returns null instead of NPE when either the MyClass instance or the someMethod() call is null
 *     Function&lt;MyClass,RetType1&gt; func1 = functionFor( MY_CLASS.someMethod().chainedWithRetType1(), safeNav() );
 *
 *     // The following returns a default value when the chainedRetType1() call results in null
 *     Function&lt;MyClass,RetType1&gt; func2 = functionFor( MY_CLASS.someMethod().chainedWithRetType1(), tailDefault(DEFAULT_RET1) );
 * </code>
 * </pre>
 * <p>
 */
public class Modes {

    /**
     * Returns a basic untyped <code>Mode</code> that provides a plain <code>BasicFunctor</code>
     * @see org.funcito.functorbase.BasicFunctor
     */
    public static Mode noOp() {
        return NoOp.NO_OP;
    }

    /**
     * Returns a <code>TypedMode</code> that provides a <code>SafeNavFunctor</code>
     * @param defaultForNull the typed value to be provided as a default for safe-navigation operation
     * @see #safeNav()
     * @see org.funcito.functorbase.SafeNavFunctor
     */
    public static <V> TypedMode<V> safeNav(V defaultForNull) {
        return new TypedSafeNav<V>(defaultForNull);
    }

    /**
     * Returns an untyped <code>Mode</code> that provides a <code>SafeNavFunctor</code> with a default value of <code>null</code>
     * @see #safeNav(Object)
     * @see org.funcito.functorbase.SafeNavFunctor
     */
    public static Mode safeNav() {
        return SafeNav.SAFE_NAV;
    }

    /**
     * Returns a <code>TypedMode</code> that provides a <code>TailDefaultFunctor</code>
     * @param defaultForNull the typed value to be provided as a default when null is the tail-value at evaluation
     * @see org.funcito.functorbase.TailDefaultFunctor
     */
    public static <V> TypedMode<V> tailDefault(V defaultForNull) {
        return new TailDefault<V>(defaultForNull);
    }

}
