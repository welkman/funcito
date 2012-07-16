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

import org.funcito.FuncitoException;
import org.funcito.internal.stub.ProxyFactory;

public class CglibProxyFactory extends ProxyFactory {

    private final CglibMethodInterceptor interceptor = new CglibMethodInterceptor();

    protected <T> T proxyImpl(Class<T> clazz, Class<?>... additionalInterfaces) {
        CglibImposterizer imposterizer = CglibImposterizer.INSTANCE;
        if (!canImposterise(clazz)) {
            throw new FuncitoException("Cannot proxy this class [" + clazz.getName() +"].  Typical causes: final class, anonymous class, or primitive class.");
        }
        return imposterizer.imposterise(interceptor, clazz, additionalInterfaces);
    }

}
