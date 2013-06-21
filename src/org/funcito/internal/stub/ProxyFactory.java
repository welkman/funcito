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
     * Exists for benchmarks suite, to try with different proxy providers
     */
    public static void reset() {
        instance = null;
    }

    public <T> T proxy(Class<T> clazz, Class<?>... additionalInterfaces) {
        T proxy = clazz.cast(proxyCache.get(clazz));
        if (proxy == null || (additionalInterfaces!=null && additionalInterfaces.length>0)) {
            proxy = proxyImpl(clazz, additionalInterfaces);
            proxyCache.put(clazz, proxy);
        }
        return proxy;
    }

    protected abstract <T> T proxyImpl(Class<T> clazz, Class<?>... additionalInterfaces);

    public boolean canImposterise(Class<?> type) {
        return !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
    }
}
