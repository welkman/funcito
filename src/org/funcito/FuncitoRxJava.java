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
package org.funcito;

import org.funcito.modifier.Modifier;
import org.funcito.modifier.UntypedModifier;
import org.funcito.rxjava.RxJavaDelegate;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

/**
 * This class is the static entry point of the Funcito API for the Netflix RxJava framework.
 */
public class FuncitoRxJava {

    private static final RxJavaDelegate rxJavaDelegate = new RxJavaDelegate();

    private FuncitoRxJava() {}

    /**
     * Generates a proxy object for use with other <code>FuncitoRxJava</code> static methods.  This proxy should not
     * be used for any other purposes.  It is a convenience pass-thru to
     * {@link org.funcito.internal.FuncitoDelegate#callsTo(Class)}.  Example usages are:
     * <p>
     * <code>
     *     // inlined: <br>
     *     Func1&lt;MyClass,RetType1&gt; func = func1For( callsTo(MyClass.class).methodWithRetType1() );<br>
     *     <br>
     *     // or extracted for repeat usage<br>
     *     final MyClass CALLS_TO = callsTo(MyClass.class);<br>
     *     Func1&lt;MyClass,RetType1&gt; func2 = func1For( CALLS_TO.method1WithRetType1() );<br>
     *     Func1&lt;MyClass,RetType2&gt; func3 = func1For( CALLS_TO.method2WithRetType2() );<br>
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a single proxy
     * can also be reused for creating multiple RxJava <code>Func1</code> objects.
     * @param clazz is the class to be proxied
     * @return a proxy which can be used by other <code>FuncitoRxJava</code> static methods
     */
    public static <T> T callsTo(Class<T> clazz) {
        return rxJavaDelegate.callsTo(clazz);
    }

    /**
     * Generates a <strong>RxJava</strong> <code>Func1</code> object that wraps a method call or method chain.  Resulting
     * <code>Func1</code> is as thread-safe as the method itself.  Example usage is:
     * <p>
     * <code>
     *     Func1&lt;MyClass,RetType1&gt; func = func1For( callsTo(MyClass.class).methodWithRetType1() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the Func1 and may not be changed:
     * <p>
     * <code>
     *     Func1&lt;MyClass,RetType1&gt; func = func1For( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br>
     *     // all invocations of func will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Func1&lt;MyClass,RetType2&gt; func = func1For( callsTo.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from a method call to a <code>FuncitoRxJava</code> proxy object
     * @return a RxJava <code>Func1</code> object that wraps the method call or chain.
     */
    public static <T,V>Func1<T,V> func1For(V proxiedMethodCall) {
        return rxJavaDelegate.func1For(proxiedMethodCall);
    }

    // TODO: javadoc
    @SuppressWarnings("unchecked")
    public static <T,V> Func1<T,V> func1For(V proxiedMethodCall, Modifier<?,V> mod) {
        return rxJavaDelegate.func1For(proxiedMethodCall, (Modifier<T, V>) mod);
    }

    // TODO: javadoc
    public static <T,V> Func1<T,V> func1For(V proxiedMethodCall, UntypedModifier mod) {
        return rxJavaDelegate.func1For(proxiedMethodCall, mod);
    }

    /**
     * Generates a <strong>RxJava</strong> <code>Action1</code> object that wraps a method call or method chain.  Resulting
     * <code>Action1</code> is as thread-safe as the method/chain itself.  This Action1 generator can only be used for
     * method calls/chains with a non-void return type, even though the return type is ignored.  For a Action1
     * generator that works with void returning methods/chains, see {@link #voidAction1()}.  Example usage is:
     * <p>
     * <code>
     *     Action1&lt;MyClass&gt; cmd = callbackFor( callsTo(MyClass.class).methodWithNonVoidReturnType() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>Action1</code> and may not be changed:
     * <p>
     * <code>
     *     Action1&lt;MyClass&gt; action = action1For( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br/>
     *     // all invocations of action will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callMyClass = callsTo(MyClass.class);<br/>
     *     Action1&lt;MyClass&gt; action = action1For( callsMyClass.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from an inlined method call to a <code>FuncitoFJ</code> proxy object
     * @param <T> is the input type of the Action1
     * @return a RxJava <code>Action1</code> object that wraps a method call or chain.
     * @see #voidAction1()
     */
    public static <T> Action1<T> action1For(Object proxiedMethodCall) {
        return     rxJavaDelegate.action1For(proxiedMethodCall);
    }

    /**
     * Prepares a method-call or method-chain call that terminates with a void return type, for generation of an
     * <strong>RxJava</strong> <code>Action1</code> object.  Use of this method is paired with a following
     * execution of one of the void-generating methods ({@link #voidAction1()} or {@link #voidAction1(Class)}). Resulting
     * <code>Action1</code> is as thread-safe as the method/chain itself.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the <code>Action1</code> and may not be changed:
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
     * @param <T> is the input type of the Action1 being prepared
     * @see #voidAction1(Class)
     * @see #voidAction1()
     */
    public static <T> T prepareVoid(T t) {
        return     rxJavaDelegate.prepareVoid(t);
    }

    /**
     * Generates a <strong>RxJava</strong> <code>Action1</code> object that wraps a method call or method chain.  Resulting
     * <code>Action1</code> is as thread-safe as the method/chain itself.  This Action1 generator is only  appropriate for
     * method calls/chains with a void return type, and it requires previous usage of {@link #prepareVoid(Object)}.
     * There is a safer overloaded form of this method that uses a target Class type to validate that the generated
     * Action1 is assigned to an appropriately type-constrained Action1.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();<br/>
     *     Action1&lt;MyClass&gt; action = voidAction1();
     * </code>
     * <p>
     * @return an RxJava <code>Action1</code> object that wraps a previously prepared method call or chain.
     * @param <T> is the input type of the Action1
     * @see #voidAction1(Class)
     * @see #prepareVoid(Object)
     */
    public static <T> Action1<T> voidAction1() {
        return     rxJavaDelegate.voidAction1();
    }

    /**
     * Generates a <strong>RxJava</strong> <code>Action1</code> object that wraps a method call or method chain.  Resulting
     * <code>Action1</code> is as thread-safe as the method/chain itself.  This Action1 generator is only appropriate
     * for method calls/chains with a void return type, and it requires previous usage of {@link #prepareVoid(Object)}.
     * This is the overloaded and safer form of {@link #voidAction1()}, which uses a target Class type to validate that
     * the generated Action1 is assigned to an appropriately type-constrained Action1.  Example usage is:
     * <p>
     * <code>
     *     prepareVoid(callsTo(MyClass.class)).methodWithVoidReturnType();<br/>
     *     Action1&lt;MyClass&gt; action = voidAction1(MyClass.class); // added safety in assignment
     * </code>
     * <p>
     * @return an RxJava <code>Action1</code> object that wraps a previously prepared method call or chain.
     * @param c the input target Class for validation of the assigned constraint-type of the Action1.
     * @param <T> is the input type of the Action1
     * @see #voidAction1()
     * @see #prepareVoid(Object)
     */
    public static <T> Action1<T> voidAction1(Class<T> c) {
        return     rxJavaDelegate.voidAction1(c);
    }


    static RxJavaDelegate delegate() {
        return rxJavaDelegate;
    }
}
