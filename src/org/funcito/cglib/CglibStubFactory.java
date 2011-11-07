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
import net.sf.cglib.proxy.Enhancer;
import org.funcito.FuncitoException;
import org.funcito.StubFactory;

import java.lang.reflect.Constructor;
import java.util.Map;

@GwtIncompatible(value="Depends on CGLib bytecode generation library")
public class CglibStubFactory extends StubFactory {

    private Map<Class, Object> stubsCache = Maps.newHashMap();

    public <T> T stub(Class<T> clazz) {
        if (stubsCache.containsKey(clazz)) {
            return (T) stubsCache.get(clazz);
        }
        // TODO: investigate, enhancer may have its own cache, see enhancer.setUseCache(boolean)
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new CglibQueingInterceptor());
        T stub = null;
        try {
            // check for interface or no-arg constructor
            try {
                if (clazz.isInterface() || clazz.getDeclaredConstructor()!=null) {
                    stub  = (T)enhancer.create();
                }
            // failure to find no-arg constructor will throw exception, forcing search for other constructors
            } catch (Exception e) {
                Constructor<T> ctor = (Constructor<T>)clazz.getDeclaredConstructors()[0];
                Class<?>[] parmTypes = ctor.getParameterTypes();
                Object[] args = new Object[parmTypes.length];
                // TODO: this may not work when the particular CTOR requires 1 or more non-null args
                stub = (T)enhancer.create(parmTypes, args);
            }
        } catch (Exception e2) {
            throw new FuncitoException("error stubbing class " + clazz.getName(), e2);
        }
        stubsCache.put(clazz, stub);
        return stub;
    }

}
