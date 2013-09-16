package org.funcito;

import fj.Effect;
import fj.F;
import org.funcito.functionaljava.FJDelegate;
import org.funcito.mode.Mode;
import org.funcito.mode.UntypedMode;

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

/**
 * This class is the static entry point of the Funcito API for <strong>Functional Java</strong>.
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
     * @param <T> is the type to be proxied
     * @return a proxy which can be used by other <code>FuncitoFJ</code> static methods
     */
    public static <T> T callsTo(Class<T> clazz) {
        return fjDelegate.callsTo(clazz);
    }

    /**
     * Generates a <strong>Functional Java</strong> <code>F</code> (<i>function</i>) object that wraps a method call or method chain.  Resulting
     * <code>F</code> is as thread-safe as the method/chain itself.  Example usage is:
     * <p>
     * <code>
     *     F&lt;MyClass,RetType1&gt; func = fFor( callsTo(MyClass.class).methodWithRetType1() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>F</code> and may not be changed:
     * <p>
     * <code>
     *     F&lt;MyClass,RetType1&gt; func = fFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br/>
     *     // all invocations of func will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callMyClass = callsTo(MyClass.class);<br/>
     *     F&lt;MyClass,RetType2&gt; func = fFor( callMyClass.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from a method call to a <code>FuncitoFJ</code> proxy object
     * @param <T> is the input type
     * @param <V> is the end return type
     * @return a Functional Java <code>F</code> object that wraps the method call or chain.
     */
    public static <T,V> F<T,V> fFor(V proxiedMethodCall) {
        return fjDelegate.fFor(proxiedMethodCall);
    }

    // TODO: javadoc
    @SuppressWarnings("unchecked")
    public static <T,V> F<T,V> fFor(V proxiedMethodCall, Mode<?,V> mode) {
        return fjDelegate.fFor(proxiedMethodCall, (Mode<T, V>) mode);
    }

    // TODO: javadoc
    public static <T,V> F<T,V> fFor(V proxiedMethodCall, UntypedMode mode) {
        return fjDelegate.fFor(proxiedMethodCall, mode);
    }

    /**
     * Generates a <strong>Functional Java</strong> <code>Effect</code> object that wraps a method call or method chain.  Resulting
     * <code>Effect</code> is as thread-safe as the method/chain itself.  This Effect generator can only be used for
     * method calls/chains with a non-void return type, even though the return type is ignored.  For an Effect
     * generator that works with void returning methods/chains, see {@link #voidEffect()}.  Example usage is:
     * <p>
     * <code>
     *     Effect&lt;MyClass&gt; effect = effectFor( callsTo(MyClass.class).methodWithNonVoidReturnType() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>Effect</code> and may not be changed:
     * <p>
     * <code>
     *     Effect&lt;MyClass&gt; effect = effectFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br/>
     *     // all invocations of effect will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callMyClass = callsTo(MyClass.class);<br/>
     *     Effect&lt;MyClass&gt; func = effectFor( callMyClass.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from an inlined method call to a <code>FuncitoFJ</code> proxy object
     * @param <T> is the input type of the Effect
     * @return a Functional Java <code>Effect</code> object that wraps a method call or chain.
     * @see #voidEffect()
     */
    public static <T> Effect<T> effectFor(Object proxiedMethodCall) {
        return fjDelegate.effectFor(proxiedMethodCall);
    }

    public static <T> Effect<T> effectFor(Object proxiedMethodCall, Mode<T,Void> mode) {
        return fjDelegate.effectFor(proxiedMethodCall, mode);
    }

    public static <T> Effect<T> effectFor(Object proxiedMethodCall, UntypedMode mode) {
        return fjDelegate.effectFor(proxiedMethodCall, mode);
    }

    /**
     * Prepares a method-call or method-chain call that terminates with a void return type, for generation of a
     * <strong>Functional Java</strong> <code>Effect</code> object.  Use of this method is paired with a following
     * execution of the void-generating method ({@link #voidEffect()}. Resulting <code>Effect</code> is as thread-safe
     * as the method/chain itself.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>Effect</code> and may not be changed:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).voidMethodWithArgs("abc", 123L);<br/>
     *     // all invocations of effect will use "abc" and 123L as the arguments to voidMethodWithArgs
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
     * @param <T> is the input type of the Effect being prepared
     * @see #voidEffect()
     */
    public static <T> T prepareVoid(T t) {
        return fjDelegate.prepareVoid(t);
    }

    /**
     * Generates a <strong>Functional Java</strong> <code>Effect</code> object that wraps a method call or method chain.  Resulting
     * <code>Effect</code> is as thread-safe as the method/chain itself.  This Effect generator is only  appropriate for
     * method calls/chains with a void return type, and it requires previous usage of {@link #prepareVoid(Object)}.
     * There is a safer overloaded form of this method that uses a target Class type to validate that the generated
     * Effect is assigned to an appropriately type-constrained Effect.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();<br/>
     *     Effect&lt;MyClass&gt; effect = voidEffect();
     * </code>
     * <p>
     * @return a Functional Java <code>Effect</code> object that wraps a previously prepared method call or chain.
     * @param <T> is the input type of the Effect
     * @see #prepareVoid(Object)
     */
    public static <T> Effect<T> voidEffect() {
        return fjDelegate.voidEffect();
    }

    public static <T> Effect<T> voidEffect(Mode<T,Void> mode) {
        return fjDelegate.voidEffect(mode);
    }

    public static <T> Effect<T> voidEffect(UntypedMode mode) {
        return fjDelegate.voidEffect(mode);
    }

    static FJDelegate delegate() {
        return fjDelegate;
    }
}
