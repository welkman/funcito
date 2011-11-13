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

package org.funcito.cglib;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Maps;
import org.funcito.FuncitoException;
import org.funcito.StubFactory;

import java.util.Map;

@GwtIncompatible(value="Depends on CGLib bytecode generation library")
public class CglibStubFactory extends StubFactory {

    private Map<Class, Object> stubsCache = Maps.newHashMap();

    public <T> T stub(Class<T> clazz) {
        T stub = (T)stubsCache.get(clazz);
        if (stub == null) {
            ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
            if (!imposterizer.canImposterise(clazz)) {
                throw new FuncitoException("Cannot mock this class");
            }
            stub = imposterizer.imposterise(new CglibQueingInterceptor(), clazz);
            stubsCache.put(clazz, stub);
        }
        return stub;
    }

}
