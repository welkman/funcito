package org.funcito;

/*
 * Copyright 2012 Project Funcito Contributors
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

import org.funcito.play.Play2Delegate;
import play.libs.F.Function;

/**
 * This class is the static entry point of the Funcito API for the Play! Framework 2.
 */
public class FuncitoPlay2 {

    private static final Play2Delegate play2Delegate = new Play2Delegate();

    private FuncitoPlay2() {}

    /**
     * Generates a proxy object for use with other <code>FuncitoPlay2</code> static methods.  This proxy should not
     * be used for any other purposes.  It is a convenience pass-thru to
     * {@link org.funcito.internal.FuncitoDelegate#callsTo(Class)}.  Example usages are:
     * <p>
     * <code>
     *     // inlined: <br>
     *     Function&lt;MyClass,RetType1&gt; func = functionFor( callsTo(MyClass.class).methodWithRetType1() );<br>
     *     <br>
     *     // or extracted for repeat usage<br>
     *     final MyClass CALLS_TO = callsTo(MyClass.class);<br>
     *     Function&lt;MyClass,RetType1&gt; func2 = functionFor( CALLS_TO.method1WithRetType1() );<br>
     *     Function&lt;MyClass,RetType2&gt; func3 = functionFor( CALLS_TO.method2WithRetType2() );<br>
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a single proxy
     * can also be reused for creating multiple Play! <code>Function</code> objects.
     * @param clazz is the class to be proxied
     * @return a proxy which can be used by other <code>FuncitoPlay2</code> static methods
     */
    public static <T> T callsTo(Class<T> clazz) {
        return play2Delegate.callsTo(clazz);
    }

    /**
     * Generates a Play! Framework <code>Function</code> object that wraps a method call or method chain.  Resulting
     * <code>Function</code> is as thread-safe as the method itself.  Example usage is:
     * <p>
     * <code>
     *     Function&lt;MyClass,RetType1&gt; func = functionFor( callsTo(MyClass.class).methodWithRetType1() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the Function and may not be changed:
     * <p>
     * <code>
     *     Function&lt;MyClass,RetType1&gt; func = functionFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br>
     *     // all invocations of func will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Function&lt;MyClass,RetType2&gt; func = functionFor( callsTo.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from a method call to a <code>FuncitoPlay2</code> proxy object
     * @return a Guava <code>Function</code> object that wraps the method call or chain.
     */
    public static <T,V>Function<T,V> functionFor(V proxiedMethodCall) {
        return play2Delegate.functionFor(proxiedMethodCall);
    }

    static Play2Delegate delegate() {
        return play2Delegate;
    }
}
