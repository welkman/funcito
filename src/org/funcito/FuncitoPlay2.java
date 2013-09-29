/*
 * Copyright 2012-2013 Project Funcito Contributors
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

import org.funcito.mode.Mode;
import org.funcito.mode.TypedMode;
import org.funcito.play.Play2Delegate;
import play.libs.F.Function;
import play.libs.F.Callback;

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
     * Generates a <strong>Play! Framework</strong> <code>Function</code> object that wraps a method call or method chain.  Resulting
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
     * @return a Play! 2 <code>Function</code> object that wraps the method call or chain.
     */
    public static <T,V>Function<T,V> functionFor(V proxiedMethodCall) {
        return play2Delegate.functionFor(proxiedMethodCall);
    }

    // TODO: javadoc
    @SuppressWarnings("unchecked")
    public static <T,V> Function<T,V> functionFor(V proxiedMethodCall, TypedMode<V> mode) {
        return play2Delegate.functionFor(proxiedMethodCall, (TypedMode<V>) mode);
    }

    // TODO: javadoc
    public static <T,V> Function<T,V> functionFor(V proxiedMethodCall, Mode mode) {
        return play2Delegate.functionFor(proxiedMethodCall, mode);
    }

    /**
     * Generates a <strong>Play! 2</strong> <code>Callback</code> object that wraps a method call or method chain.  Resulting
     * <code>Callback</code> is as thread-safe as the method/chain itself.  This Callback generator can only be used for
     * method calls/chains with a non-void return type, even though the return type is ignored.  For a Callback
     * generator that works with void returning methods/chains, see {@link #voidCallback()}.  Example usage is:
     * <p>
     * <code>
     *     Callback&lt;MyClass&gt; cmd = callbackFor( callsTo(MyClass.class).methodWithNonVoidReturnType() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>Callback</code> and may not be changed:
     * <p>
     * <code>
     *     Callback&lt;MyClass&gt; cmd = callbackFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br/>
     *     // all invocations of callback will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callMyClass = callsTo(MyClass.class);<br/>
     *     Callback&lt;MyClass&gt; cmd = callbackFor( callsMyClass.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from an inlined method call to a <code>FuncitoFJ</code> proxy object
     * @param <T> is the input type of the Callback
     * @return a Play! 2 <code>Callback</code> object that wraps a method call or chain.
     * @see #voidCallback()
     */
    public static <T> Callback<T> callbackFor(Object proxiedMethodCall) {
        return     play2Delegate.callbackFor(proxiedMethodCall);
    }

    public static <T> Callback<T> callbackFor(Object proxiedMethodCall, TypedMode<Void> mode) {
        return     play2Delegate.callbackFor(proxiedMethodCall, mode);
    }

    public static <T> Callback<T> callbackFor(Object proxiedMethodCall, Mode mode) {
        return     play2Delegate.callbackFor(proxiedMethodCall, mode);
    }

    /**
     * Prepares a method-call or method-chain call that terminates with a void return type, for generation of a
     * <strong>Play! 2</strong> <code>Callback</code> object.  Use of this method is paired with a following
     * execution of one of the void-generating methods ({@link #voidCallback()} or {@link #voidCallback(Class)}). Resulting
     * <code>Callback</code> is as thread-safe as the method/chain itself.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>Callback</code> and may not be changed:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).voidMethodWithArgs("abc", 123L);<br/>
     *     // all invocations of callback will use "abc" and 123L as the arguments to voidMethodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callMyClass = callsTo(MyClass.class);<br>
     *     prepareVoid(callsMyClass).methodWithRetType1().voidMethod();<br/>
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (final return type does not matter because it is
     * void) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable.  Intermediate return types also cannot be a Java
     * Generic type because of type erasure.
     * <p>
     * @return the same Funcito proxy object that is passed in.  Provided for fluent API so that desired method chain
     * call may be directly appended.
     * @param <T> is the input type of the Callback being prepared
     * @see #voidCallback()
     */
    public static <T> T prepareVoid(T t) {
        return     play2Delegate.prepareVoid(t);
    }

    /**
     * Generates a <strong>Play! 2</strong> <code>Callback</code> object that wraps a method call or method chain.  Resulting
     * <code>Callback</code> is as thread-safe as the method/chain itself.  This Callback generator is only  appropriate for
     * method calls/chains with a void return type, and it requires previous usage of {@link #prepareVoid(Object)}.
     * There is a safer overloaded form of this method that uses a target Class type to validate that the generated
     * Callback is assigned to an appropriately type-constrained Callback.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();<br/>
     *     Callback&lt;MyClass&gt; cmd = voidCallback();
     * </code>
     * <p>
     * @return a Play! 2 <code>Callback</code> object that wraps a previously prepared method call or chain.
     * @param <T> is the input type of the Callback
     * @see #prepareVoid(Object)
     */
    public static <T> Callback<T> voidCallback() {
        return     play2Delegate.voidCallback();
    }

    public static <T> Callback<T> voidCallback(TypedMode<Void> mode) {
        return     play2Delegate.voidCallback(mode);
    }

    public static <T> Callback<T> voidCallback(Mode mode) {
        return     play2Delegate.voidCallback(mode);
    }

    static Play2Delegate delegate() {
        return play2Delegate;
    }
}
