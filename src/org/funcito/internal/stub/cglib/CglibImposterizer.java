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

/* Copied in large part from project Mockito, with the following license:
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.funcito.internal.stub.cglib;

import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.*;
import org.funcito.FuncitoException;
import org.funcito.internal.stub.AbstractClassImposterizer;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Thanks to Mockito guys (and indirectly, jMock) for this handy class that wraps all the cglib magic.
 */
public class CglibImposterizer extends AbstractClassImposterizer {

    public static final CglibImposterizer INSTANCE = new CglibImposterizer();

    private ObjenesisStd objenesis = new ObjenesisStd();

    private CglibImposterizer() {}

    private static final NamingPolicy NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES = new CglibNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };

    public <T> T imposterise(final MethodInterceptor interceptor, Class<T> mockedType) {
        try {
            setConstructorsAccessible(mockedType, true);
            Class<?> proxyClass = createProxyClass(mockedType);
            return mockedType.cast(createProxy(proxyClass, interceptor));
        } finally {
            setConstructorsAccessible(mockedType, false);
        }
    }

    private <T> Class<?> createProxyClass(Class<?> mockedType) {
        // NOTE: this was part of the original ClassImposterizer, but it doesn't seem to be needed
//        if (mockedType == Object.class) {
//            mockedType = ClassWithSuperclassToWorkAroundCglibBug.class;
//        }

        Enhancer enhancer = new Enhancer() {
            @Override
            @SuppressWarnings("unchecked")
            protected void filterConstructors(Class sc, List constructors) {
                // Don't filter
            }
        };
        enhancer.setClassLoader(SearchingClassLoader.combineLoadersOf(mockedType));
        enhancer.setUseFactory(true);
        if (mockedType.isInterface()) {
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(prepend(mockedType));
        } else {
            enhancer.setSuperclass(mockedType);
        }
//        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class, NoOp.class});
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
        // we want to handle bridge methods so don't filter them
//        enhancer.setCallbackFilter(IGNORE_BRIDGE_METHODS);
        if (mockedType.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        } else {
            enhancer.setNamingPolicy(CglibNamingPolicy.INSTANCE);
        }

        try {
            return enhancer.createClass();
        } catch (CodeGenerationException e) {
            if (Modifier.isPrivate(mockedType.getModifiers())) {
                throw new FuncitoException("\n"
                        + "Funcito cannot mock this class: " + mockedType
                        + ".\n"
                        + "Most likely it is a private class that is not visible by Funcito");
            }
            throw new FuncitoException("\n"
                    + "Funcito cannot mock this class: " + mockedType
                    + ".\n"
                    + "Funcito can only mock visible & non-final classes");
        }
    }

    private Object createProxy(Class<?> proxyClass, final MethodInterceptor interceptor) {
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[]{interceptor});
//        proxy.setCallbacks(new Callback[] {interceptor, NoOp.INSTANCE});
        return proxy;
    }

    private static class ClassWithSuperclassToWorkAroundCglibBug {
    }
}
