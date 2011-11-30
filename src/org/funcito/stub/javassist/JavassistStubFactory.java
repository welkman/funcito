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

package org.funcito.stub.javassist;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Maps;
import org.funcito.FuncitoException;
import org.funcito.stub.StubFactory;
import org.funcito.stub.javassist.internal.JavassistImposterizer;
import org.funcito.stub.javassist.internal.JavassistMethodHandler;

import java.util.Map;

@GwtIncompatible(value = "Depends on Javassist bytecode generation library")
public class JavassistStubFactory extends StubFactory {

    private Map<Class, Object> stubsCache = Maps.newHashMap();
    private final JavassistMethodHandler handler = new JavassistMethodHandler();

    public <T> T stub(Class<T> clazz) {
        T stub = clazz.cast(stubsCache.get(clazz));
        if (stub == null) {

            JavassistImposterizer imposterizer = JavassistImposterizer.INSTANCE;
            if (!imposterizer.canImposterise(clazz)) {
                throw new FuncitoException("Cannot mock this class");
            }
            stub = imposterizer.imposterise(handler, clazz);
            stubsCache.put(clazz, stub);
        }
        return stub;
    }

}
