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
 * This class is the static entry point of the Funcito API for Functional Java.
 */
public class FuncitoFJ {
    private static final FJDelegate fjDelegate = new FJDelegate();

    private FuncitoFJ() {}

    /**
     * Generates a proxy object for use with other <code>FuncitoFJ</code> static methods.  This proxy should not
     * be used for any other purposes.  It is a convenience pass-thru to
     * {@link org.funcito.internal.FuncitoDelegate#callsTo(Class)}.  Example usages are:
     * <p>
     * <code>
     *     // inlined: <br>
     *     F&lt;MyClass,RetType1&gt; func = fFor( callsTo(MyClass.class).methodWithRetType1() );<br>
     *     <br>
     *     // or extracted for repeat usage<br>
     *     final MyClass CALLS_TO = callsTo(MyClass.class);<br>
     *     F&lt;MyClass,RetType1&gt; func2 = fFor( CALLS_TO.method1WithRetType1() );<br>
     *     F&lt;MyClass,RetType2&gt; func3 = fFor( CALLS_TO.method2WithRetType2() );<br>
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a single proxy
     * can also be reused for creating multiple Functional Java <code>F</code> objects.
     * @param clazz is the class to be proxied
     * @return a proxy which can be used by other <code>FuncitoFJ</code> static methods
     */
    public static <T> T callsTo(Class<T> clazz) {
        return fjDelegate.callsTo(clazz);
    }

    /**
     * Generates a Functional Java <code>F</code> (<i>function</i>) object that wraps a method call or method chain.  Resulting
     * <code>F</code> is as thread-safe as the method itself.  Example usage is:
     * <p>
     * <code>
     *     F&lt;MyClass,RetType1&gt; func = fFor( callsTo(MyClass.class).methodWithRetType1() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the F and may not be changed:
     * <p>
     * <code>
     *     F&lt;MyClass,RetType1&gt; func = fFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br>
     *     // all invocations of func will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     F&lt;MyClass,RetType2&gt; func = fFor( callsTo.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from a method call to a <code>FuncitoFJ</code> proxy object
     * @return a Functional Java <code>F</code> object that wraps the method call or chain.
     */
    public static <T,V> F<T,V> fFor(V proxiedMethodCall) {
        return fjDelegate.fFor(proxiedMethodCall);
    }

    static FJDelegate delegate() {
        return fjDelegate;
    }
}
