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
 * This class is the static entry point of the Funcito API for Jedi.
 */
public class FuncitoJedi {

    private static final JediDelegate jediDelegate = new JediDelegate();

    private FuncitoJedi() {}

    /**
     * Generates a proxy object for use with other <code>FuncitoJedi</code> static methods.  This proxy should not
     * be used for any other purposes.  It is a convenience pass-thru to
     * {@link org.funcito.internal.FuncitoDelegate#callsTo(Class)}.  Example usages are:
     * <p>
     * <code>
     *     // inlined: <br>
     *     Functor&lt;MyClass,RetType1&gt; func = functorFor( callsTo(MyClass.class).methodWithRetType1() );<br>
     *     <br>
     *     // or extracted for repeat usage<br>
     *     final MyClass CALLS_TO = callsTo(MyClass.class);<br>
     *     Functor&lt;MyClass,RetType1&gt; func2 = functorFor( CALLS_TO.method1WithRetType1() );<br>
     *     Functor&lt;MyClass,RetType2&gt; func3 = functorFor( CALLS_TO.method2WithRetType2() );<br>
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a single proxy
     * can also be reused for creating multiple Jedi <code>Functor</code> or <code>Filter</code> objects.
     * @param clazz is the class to be proxied
     * @return a proxy which can be used by other <code>FuncitoJedi</code> static methods
     */
    public static <T> T callsTo(Class<T> clazz) {
        return jediDelegate.callsTo(clazz);
    }

    /**
     * Generates a Jedi <code>Functor</code> object that wraps a method call or method chain.  Resulting
     * <code>Functor</code> is as thread-safe as the method itself.  Example usage is:
     * <p>
     * <code>
     *     Functor&lt;MyClass,RetType1&gt; func = functorFor( callsTo(MyClass.class).methodWithRetType1() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the Function and may not be changed:
     * <p>
     * <code>
     *     Functor&lt;MyClass,RetType1&gt; func = functorFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br>
     *     // all invocations of func will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Functor&lt;MyClass,RetType2&gt; func = functorFor( callsTo.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from a method call to a <code>FuncitoJedi</code> proxy object
     * @return a Jedi <code>Functor</code> object that wraps the method call or chain.
     */
    public static <T,V> Functor<T, V> functorFor(V proxiedMethodCall) {
        return jediDelegate.functorFor(proxiedMethodCall);
    }

    /**
     * Generates a Jedi <code>Filter</code> object that wraps a <code>Boolean</code>- or <code>boolean</code>-return method call
     * or method chain.  Resulting <code>Filter</code> is as thread-safe as the method itself.
     * Auto-boxing means you may always safely wrap a method that has a primitive boolean return type. Example usage
     * is:
     * <p>
     * <code>
     *     Filter&lt;MyClass&gt; filter = filterFor( callsTo(MyClass.class).methodWithBoolRetType() );
     * </code>
     * <p>
     * Methods returning either boolean primitive or Boolean wrapper objects may be wrapped without problems.
     * <p>
     * This method also supports wrapping methods with arguments, and method chaining, as documented in {@link #functorFor(Object)}.
     * @param proxiedMethodCall is the Boolean return value from a method call to a <code>FuncitoJedi</code> proxy object
     * @return a Jedi <code>Filter</code> object that wraps the method call or method chain.
     */
    public static <T> Filter<T> filterFor(Boolean proxiedMethodCall) {
        return jediDelegate.filterFor(proxiedMethodCall);
    }

    public static JediDelegate delegate() {
        return jediDelegate;
    }
}
