/**
 * Copyright 2011 Project Funcito Contributors
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.funcito.internal.stub;

import org.funcito.internal.stub.utils.ProxyUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class ProxyFactory {
    private static ProxyFactory instance = null;
    private static ProxyUtils proxyUtils = new ProxyUtils();
    private Map<Class, Object> proxyCache = new HashMap<Class, Object>();

    /**
     * Singleton method for obtaining <code>ProxyFactory</code> instance.  If value has not been set explicitly, it
     * looks to the <code>FUNCITO_PROXY_PROVIDER_PROP</code> system property for a string value to select one of the
     * three pre-defined proxy providers: "CGLIB", "JAVASSIST", or "JAVAPROXY".  If the system property is not defined,
     * the classpath is checked first in priority for CGLIB, then for Javassist, finaly falling back to the less capable
     * native Java dynamic proxies.
     */
    public static ProxyFactory instance() {
        if (instance == null) {
            instance = proxyUtils.getOverrideBySystemProperty();
            if (instance == null) {
                instance = proxyUtils.getExactlyOneFactoryFromClasspath();
            }
        }
        return instance;
    }

    /**
     * This method can be used to set custom ProxyFactory implementations.  The only recommended use is if the program
     * environment cannot define a system property or it cannot be accessed from within the program.  Also, it
     * should only be called once per program, or else results will be unpredictable.
     * @param factory The one-time provided factory for the program instantiation.
     * @see org.funcito.Funcito#setProxyProviderProperty(String)
     */
    public static void setInstance(ProxyFactory factory) {
        instance = factory;
    }

    /**
     * Exists for benchmarks suite, to try with different proxy providers.  DO NOT USE IN REAL PROGRAMS!
     */
    public static void reset() {
        instance = null;
    }

    public <T> T proxy(Class<T> clazz) {
        T proxy = clazz.cast(proxyCache.get(clazz));
        if (proxy == null) {
            proxy = proxyImpl(clazz);
            proxyCache.put(clazz, proxy);
        }
        return proxy;
    }

    protected abstract <T> T proxyImpl(Class<T> clazz);

    public boolean canImposterise(Class<?> type) {
        return !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
    }
}
