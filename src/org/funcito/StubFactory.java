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

package org.funcito;

import org.funcito.cglib.CglibStubFactory;
import org.funcito.javassist.JavassistStubFactory;

public abstract class StubFactory {
    private static StubFactory instance = null;

    public static void setInstance(StubFactory newInstance) {
        StubFactory.instance = newInstance;
    }

    public static StubFactory instance() {
        if (instance == null) {
            try {
                Class.forName("net.sf.cglib.proxy.Enhancer");
                instance = new CglibStubFactory();
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("javassist.util.proxy.ProxyFactory");
                    instance = new JavassistStubFactory();
                } catch (ClassNotFoundException e2) {
                    throw new FuncitoException("Error: Funcito requires the use of either the CGLib or Javassist code generation libraries." +
                            "Please ensure that you have one of the two libraries in your classpath.");
                }
            }
        }
        return instance;
    }

    public abstract <T> T stub(Class<T> clazz);
}
