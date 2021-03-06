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
package org.funcito;

import org.funcito.collectionsgeneric.CollectGenDelegate;
import org.funcito.functionaljava.FJDelegate;
import org.funcito.guava.GuavaDelegate;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.stub.ProxyFactory;
import org.funcito.jedi.JediDelegate;
import org.funcito.play.Play2Delegate;
import org.funcito.rxjava.RxJavaDelegate;

/**
 * This class is a public entry point into Funcito for the supported functional framework APIs.  Most users will probably
 * prefer to use static imports from the particular API being used, such as {@link FuncitoGuava}, {@link FuncitoFJ} (Functional Java),
 * or {@link FuncitoJedi}, because the usage is more terse.  But for rare cases when multiple functional frameworks are
 * being used within the same class, the static methods within this class can be used to distinguish between them.
 * Example:
 * <p>
 * <pre>
 * <code>
 *     import static org.funcito.Funcito.*;
 *     import com.google.common.base.Function;
 *     import jedi.functional.Functor;
 *     import fj.F;
 *     import play.libs.F.Function;
 *     import org.apache.commons.collections15.Transformer;
 *     import rx.util.functions.Func1;
 *
 *     Function&lt;MyClass,RetType&gt;    function = guava().functionFor( callsTo(MyClass.class).someMethod());
 *     Functor&lt;MyClass,RetType&gt;     functor  = jedi().functorFor( callsTo(MyClass.class).someMethod());
 *     F&lt;MyClass,RetType&gt;           f        = fj().fFor( callsTo(MyClass.class).someMethod());
 *     F.Function&lt;MyClass,RetType&gt;  function = play2().functionFor( callsTo(MyClass.class).someMethod());
 *     Transformer&lt;MyClass,RetType&gt; xform    = collectGen().transformerFor( callsTo(MyClass.class).someMethod());
 *     Func1&lt;MyClass,RetType&gt;       func1    = rxJava().func1For( callsTo(MyClass.class).someMethod());
 * </code>
 * </pre>
 * @see FuncitoCollectGen
 * @see FuncitoFJ
 * @see FuncitoGuava
 * @see FuncitoJedi
 * @see FuncitoPlay2
 * @see FuncitoRxJava
 */
public class Funcito {

    /** System property that forces selection of a specific proxy provider */
    public static final String FUNCITO_PROXY_PROVIDER_PROP = "funcito.proxy.provider";

    /** Value for property {@link #FUNCITO_PROXY_PROVIDER_PROP} to force CGLib as proxy provider */
    public static final String CGLIB = "CGLIB";
    /** Value for property {@link #FUNCITO_PROXY_PROVIDER_PROP} to force Javassist as proxy provider */
    public static final String JAVASSIST = "JAVASSIST";
    /** Value for property {@link #FUNCITO_PROXY_PROVIDER_PROP} to force J2SE dynamic proxies as proxy provider */
    public static final String JAVAPROXY = "JAVAPROXY";

    private Funcito() {}

    /**
     * Creates a mock instance of a class, for the purpose of capturing and wrapping faux calls to methods in that class.
     * Return values of these faux method calls should be passed as the argument to one of the functional-object factory
     * methods, preferably inline so it is apparent in one place what method is being wrapped.  This <code>callsTo()</code>
     * method is provided as a convenience when using the static delegate methods in this class.  It is functionally
     * interchangeable with calls through to the delegates' version of <code>callsTo()</code>, such as <code>guava().callsTo()</code>.
     * @param clazz Class to be mocked
     * @param <T> the specific type of the class to be mocked
     * @return mock instance of clazz, solely for invoking and capturing faux method calls
     */
    public static <T> T callsTo(Class<T> clazz) {
        FuncitoDelegate defaultDelegate = new FuncitoDelegate();
        return defaultDelegate.callsTo(clazz);
    }

    /**
     * Delegate method used to help differentiate Guava from other Funcito supported functional frameworks
     * @return a delegate with all of the same methods as the static versions in {@link FuncitoGuava}.
     * @see FuncitoGuava
     */
    public static GuavaDelegate guava() { return FuncitoGuava.delegate(); }

    /**
     * Delegate method used to help differentiate Functional Java from other Funcito supported functional frameworks
     * @return a delegate with all of the same methods as the static versions in {@link FuncitoFJ}.
     * @see FuncitoFJ
     */
    public static FJDelegate fj() { return FuncitoFJ.delegate(); }

    /**
     * Delegate method used to help differentiate Jedi from other Funcito supported functional frameworks
     * @return a delegate with all of the same methods as the static versions in {@link FuncitoJedi}.
     * @see FuncitoJedi
     */
    public static JediDelegate jedi() { return FuncitoJedi.delegate(); }

    /**
     * Delegate method used to help differentiate Play! Framework 2 from other Funcito supported functional frameworks
     * @return a delegate with all of the same methods as the static versions in {@link FuncitoPlay2}.
     * @see FuncitoPlay2
     */
    public static Play2Delegate play2() { return FuncitoPlay2.delegate(); }

    /**
     * Delegate method used to help differentiate Collections-Generic from other Funcito supported functional frameworks
     * @return a delegate with all of the same methods as the static versions in {@link FuncitoCollectGen}.
     * @see FuncitoCollectGen
     */
    public static CollectGenDelegate collectGen() { return FuncitoCollectGen.delegate(); }

    /**
     * Delegate method used to help differentiate RxJava from other Funcito supported functional frameworks
     * @return a delegate with all of the same methods as the static versions in {@link FuncitoRxJava}.
     * @see FuncitoRxJava
     */
    public static RxJavaDelegate rxJava() { return FuncitoRxJava.delegate(); }

    /**
     * For setting the <code>FUNCITO_PROXY_PROVIDER_PROP</code> system property value.  This property determines an
     * override value for choosing a pre-defined proxy provider rather than searching in a priority-based order in the
     * classpath for a known and supported one.  This is an alternative to externally setting the property in the
     * application execution environment.  The first time the property is parsed by your program, changes to the value
     * will have no further effect on determining which proxy provider will be used.  Only one proxy provider should be
     * used for the lifetime of a program's execution, though it can readily be set per execution instance.
     * @param proxyProviderPropertyVal one of "CGLIB", "JAVASSIST", or "JAVAPROXY"
     */
    public static void setProxyProviderProperty(String proxyProviderPropertyVal) {
        System.setProperty(FUNCITO_PROXY_PROVIDER_PROP, proxyProviderPropertyVal);
    }

    /**
     * This method can be used to set custom ProxyFactory implementations.  The only other recommended use is if the
     * program environment cannot define a system property or it cannot be accessed from within the program.  Also, it
     * should only be called once per program, or else results will be unpredictable.
     * @param factory The one-time provided factory for the program instantiation.
     * @see #setProxyProviderProperty(String)
     */
    public static void setProxyProvider(ProxyFactory factory) {
        ProxyFactory.setInstance(factory);
    }
}
