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

package org.funcito.internal.stub.cglib;

import java.util.Map;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.StubFactory;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Maps;

@GwtIncompatible(value = "Depends on CGLib bytecode generation library")
public class CglibStubFactory extends StubFactory {

    private Map<Class, Object> stubsCache = Maps.newHashMap();

    private final CglibMethodInterceptor interceptor = new CglibMethodInterceptor();

    public <T> T stub(Class<T> clazz) {
        T stub = clazz.cast(stubsCache.get(clazz));
        if (stub == null) {
            CglibImposterizer imposterizer = CglibImposterizer.INSTANCE;
            if (!imposterizer.canImposterise(clazz)) {
                throw new FuncitoException("Cannot mock this class.  Typical casuse: final class, anonymous class, or primitive class.");
            }
            stub = imposterizer.imposterise(interceptor, clazz);
            stubsCache.put(clazz, stub);
        }
        return stub;
    }

}
