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

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.funcito.collectionsgeneric.CollectGenDelegate;

/**
 * This class is the static entry point of the Funcito API for Collections-Generic.
 */
public class FuncitoCollectGen {

    private static final CollectGenDelegate collectGenDelegate = new CollectGenDelegate();

    private FuncitoCollectGen() {}

    /**
     * Generates a proxy object for use with other <code>FuncitoCollectGen</code> static methods.  This proxy should not
     * be used for any other purposes.  It is a convenience pass-thru to
     * {@link org.funcito.internal.FuncitoDelegate#callsTo(Class)}.  Example usages are:
     * <p>
     * <code>
     *     // inlined: <br>
     *     Transformer&lt;MyClass,RetType1&gt; xform = transformerFor( callsTo(MyClass.class).methodWithRetType1() );<br>
     *     <br>
     *     // or extracted for repeat usage<br>
     *     final MyClass CALLS_TO = callsTo(MyClass.class);<br>
     *     Transformer&lt;MyClass,RetType1&gt; xform2 = transformerFor( CALLS_TO.method1WithRetType1() );<br>
     *     Transformer&lt;MyClass,RetType2&gt; xform3 = transformerFor( CALLS_TO.method2WithRetType2() );<br>
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a single proxy
     * can also be reused for creating multiple Collections-Generic <code>Transformer</code> or <code>Predicate</code> objects.
     * @param clazz is the class to be proxied
     * @return a proxy which can be used by other <code>FuncitoCollectGen</code> static methods
     */
    public static <T> T callsTo(Class<T> clazz) {
        return collectGenDelegate.callsTo(clazz);
    }

    /**
     * Generates a Collections-Generic <code>Transformer</code> object that wraps a method call or method chain.  Resulting
     * <code>Transformer</code> is as thread-safe as the method itself.  Example usage is:
     * <p>
     * <code>
     *     Transformer&lt;MyClass,RetType1&gt; xform = transformerFor( callsTo(MyClass.class).methodWithRetType1() );
     * </code>
     * <p>
     * You can wrap methods with parameters, so long as you statically provide values for each
     * parameter.  Provided parameter values are statically bound to the Transformer and may not be changed:
     * <p>
     * <code>
     *     Transformer&lt;MyClass,RetType1&gt; xform = transformerFor( callsTo(MyClass.class).methodWithArgs("abc", 123L) );<br>
     *     // all invocations of xform will use "abc" and 123L as the arguments to methodWithArgs
     * </code>
     * <p>
     * It is also possible to wrap method call chains, with some restrictions:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Transformer&lt;MyClass,RetType2&gt; xform = transformerFor( callsTo.methodWithRetType1().methodWithRetType2() );
     * </code>
     * <p>
     * Restrictions for chaining are that intermediate return types (all except for the final return type in the
     * chain) must be proxyable by the current Proxy provider, just like the initial target type.  In the above
     * example this means MyClass and RetType1 must be proxyable, but RetType2 need not be proxyable.
     * Intermediate return types also cannot be a Java Generic type because of type erasure.
     * <p>
     * @param proxiedMethodCall is the return value from a method call to a <code>FuncitoCollectGen</code> proxy object
     * @return a Collections-Generic <code>Transformer</code> object that wraps the method call or chain.
     */
    public static <T,V>Transformer<T,V> transformerFor(V proxiedMethodCall) {
        return collectGenDelegate.transformerFor(proxiedMethodCall);
    }

    /**
     * Generates a Collections-Generic <code>Predicate</code> object that wraps a <code>Boolean</code>- or <code>boolean</code>-return method call
     * or method chain.  Resulting <code>Predicate</code> is as thread-safe as the method itself.
     * Auto-boxing means you may always safely wrap a method that has a primitive boolean return type. Example usage
     * is:
     * <p>
     * <code>
     *     Predicate&lt;MyClass&gt; pred = predicateFor( callsTo(MyClass.class).methodWithBoolRetType() );
     * </code>
     * <p>
     * Users of this <code>Predicate</code> should be aware of a risk with Collections-Generic Predicates (not specific to Funcito) if the
     * return type of the method being wrapped is a Boolean wrapper rather than a primitive boolean.  Such calls are allowed,
     * but there is an inherent null-pointer risk because Collections-Generic <code>Predicate.evaluate(T)</code> returns a boolean primitive.
     * See overloaded form of this method {@link #predicateFor(Boolean, boolean)} for a mitigation of this risk.
     * <p>
     * This method also supports wrapping methods with arguments, and method chaining, as documented in {@link #transformerFor(Object)}.
     * @param proxiedMethodCall is the Boolean return value from a method call to a <code>FuncitoCollectGen</code> proxy object
     * @return a Collections-Generic  <code>Predicate</code> object that wraps the method call or method chain.
     * @see #predicateFor(Boolean, boolean)
     */
    public static <T>Predicate<T> predicateFor(Boolean proxiedMethodCall) {
        return collectGenDelegate.predicateFor(proxiedMethodCall);
    }

    /**
     * Generates a Collections-Generic <code>Predicate</code> object that wraps a <code>Boolean</code>- or boolean-return
     * method call or method chain.  Resulting <code>Predicate</code> is as thread-safe as the method itself.
     * This form of the method is only required when there is a risk of the wrapped method returning a null-value, in which you case
     * you provide the default value to substitute for null return values from the wrapped method.
     * <p>
     * This method also supports wrapping methods with arguments, and method chaining, as documented in {@link #transformerFor(Object)}.
     * @param proxiedMethodCall is the Boolean return value from a method call to a <code>FuncitoCollectGen</code> callsTo object
     * @param defaultForNull is the default value that <code>Predicate</code> will return if the wrapped method returns a null-value.
     * @return a Collections-Generic  <code>Predicate</code> object that wraps the method call or method chain.
     * @see #predicateFor(Boolean)
     */
    public static <T>Predicate<T> predicateFor(Boolean proxiedMethodCall, boolean defaultForNull) {
        return collectGenDelegate.predicateFor(proxiedMethodCall, defaultForNull);
    }

    static CollectGenDelegate delegate() {
        return collectGenDelegate;
    }
}
