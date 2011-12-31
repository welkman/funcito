package org.funcito;

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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.funcito.guava.GuavaDelegate;

/**
 * This class is the entry point of the Funcito API for Google Guava.
 */
public class FuncitoGuava {

    private static final GuavaDelegate guavaDelegate = new GuavaDelegate();

    /**
     * Generates a stub object for use with other <code>FuncitoGuava</code> static calls.  This stub should not
     * be used for any other purposes.  An example of proper usage is as follows:
     * <p>
     * <code>
     *     MyClass callsTo = callsTo(MyClass.class);<br>
     *     Function<MyClass,RetType> func = functionFor( callsTo.noArgMethodWithRetType() );
     * </code>
     * <p>
     * Stubs are cached, so this method can be called multiple times for the same class without penalty.  But a single stub
     * can also be reused for creating multiple Guava <code>Function</code> or <code>Predicate</code> objects.
     * @param clazz is the class to be stubbed
     * @return a stub which can be used by other <code>FuncitoGuava</code> static calls
     */
    public static <T> T callsTo(Class<T> clazz) {
        return guavaDelegate.callsTo(clazz);
    }

    /**
     * Generates a Guava <code>Function</code> object that wraps a method call.  Resulting <code>Function</code> is as thread-safe as the method itself.
     * @param stubbedMethodCall is the return value from a method call to a <code>FuncitoGuava</code> stub object
     * @return a Guava <code>Function</code> object that wraps the method call.
     */
    public static <T,V>Function<T,V> functionFor(V stubbedMethodCall) {
        return guavaDelegate.functionFor(stubbedMethodCall);
    }

    /**
     * Generates a Guava <code>Predicate</code> object that wraps a <code>Boolean</code>- or boolean-return method call.
     * Resulting <code>Predicate</code> is as thread-safe as the method itself.
     * Auto-boxing means you may always safely wrap a method that has a primitive boolean return type.
     * <p>
     * Users of this <code>Predicate</code> should be aware of the inherent risk with Guava Predicates (not specific to Funcito) if the
     * return type of the method being wrapped is a Boolean wrapper.  Such calls are allowed, but there is no
     * inherent null-pointer safety because Guava <code>Predicate.apply(T)</code> returns a boolean primitive.  But see overloaded form
     * of this method below for a mitigation of this risk.
     * @param stubbedMethodCall is the Boolean return value from a method call to a <code>FuncitoGuava</code> stub object
     * @return a Guava  <code>Predicate</code> object that wraps the method call.
     * @see #predicateFor(Boolean, boolean)
     */
    public static <T>Predicate<T> predicateFor(Boolean stubbedMethodCall) {
        return guavaDelegate.predicateFor(stubbedMethodCall);
    }

    /**
     * Generates a Guava <code>Predicate</code> object that wraps a <code>Boolean</code>- or boolean-return method call.  Resulting <code>Predicate</code>
     * is as thread-safe as the method itself.
     * This form of the method is only required when there is a risk of the wrapped method returning a null-value, in which you case
     * you provide the default value to substitute for null return values from the wrapped method.
     * @param stubbedMethodCall is the Boolean return value from a method call to a <code>FuncitoGuava</code> callsTo object
     * @param defaultForNull is the default value that <code>Predicate</code> will return if the wrapped method returns a null-value.
     * @return a Guava  <code>Predicate</code> object that wraps the method call.
     * @see #predicateFor(Boolean)
     */
    public static <T>Predicate<T> predicateFor(Boolean stubbedMethodCall, boolean defaultForNull) {
        return guavaDelegate.predicateFor(stubbedMethodCall, defaultForNull);
    }

    static GuavaDelegate delegate() {
        return guavaDelegate;
    }
}
