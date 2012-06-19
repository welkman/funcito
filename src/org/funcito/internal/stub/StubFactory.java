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

import org.funcito.internal.stub.utils.StubUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class StubFactory {
    private static StubFactory instance = null;
    private static StubUtils stubUtils = new StubUtils();
    private Map<Class, Object> stubsCache = new HashMap<Class, Object>();

    public static StubFactory instance() {
        if (instance == null) {
            instance = stubUtils.getOverrideBySystemProperty();
            if (instance == null) {
                instance = stubUtils.getExactlyOneFactoryFromClasspath();
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

    public <T> T stub(Class<T> clazz, Class<?>... additionalInterfaces) {
        T stub = clazz.cast(stubsCache.get(clazz));
        if (stub == null || (additionalInterfaces!=null && additionalInterfaces.length>0)) {
            stub = stubImpl(clazz, additionalInterfaces);
            stubsCache.put(clazz, stub);
        }
        return stub;
    }

    protected abstract <T> T stubImpl(Class<T> clazz, Class<?>... additionalInterfaces);

    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
    }
}
