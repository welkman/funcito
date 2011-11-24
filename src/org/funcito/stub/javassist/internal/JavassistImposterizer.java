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

package org.funcito.stub.javassist.internal;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.funcito.FuncitoException;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavassistImposterizer {

    public static final JavassistImposterizer INSTANCE = new JavassistImposterizer();

    private JavassistImposterizer() {
    }

    private ObjenesisStd objenesis = new ObjenesisStd();

// TODO: from CglibImposerizer, it doesn't look like there is an equivalent capability in Javassist ProxyFactory
//    private static final NamingPolicy NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES = new CglibNamingPolicy() {
//        @Override
//        public String getClassName(String prefix, String source, Object key, Predicate names) {
//            return "codegen." + super.getClassName(prefix, source, key, names);
//        }
//    };

    private static final MethodFilter IGNORE_BRIDGE_METHODS = new MethodFilter() {
        public boolean isHandled(Method method) {
            return !method.isBridge();
        }
    };

    public boolean canImposterise(Class<?> type) {
        // NOTE: It looks like Javassist maybe cannot Proxy interfaces with default access
        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
//        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
    }

    public <T> T imposterise(final MethodHandler handler, Class<T> mockedType) {
        try {
            setConstructorsAccessible(mockedType, true);
            Class<?> proxyClass = createProxyClass(mockedType);
            return mockedType.cast(createProxy(proxyClass, handler));
        } finally {
            setConstructorsAccessible(mockedType, false);
        }
    }

    private void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }

    private <T> Class<?> createProxyClass(Class<?> mockedType) {
        if (mockedType == Object.class) {
            mockedType = ClassWithSuperclassToWorkAroundCglibBug.class;
        }

        ProxyFactory factory = new ProxyFactory();
        if (mockedType.isInterface()) {
            factory.setSuperclass(Object.class);
            factory.setInterfaces(prepend(mockedType));
        } else {
            factory.setSuperclass(mockedType);
        }
//        if (mockedType.getSigners() != null) {
//            enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
//        } else {
//            enhancer.setNamingPolicy(CglibNamingPolicy.INSTANCE);
//        }

        try {
            return factory.createClass(IGNORE_BRIDGE_METHODS);
        } catch (Throwable e) {
            if (Modifier.isPrivate(mockedType.getModifiers())) {
                throw new FuncitoException("\n"
                        + "Funcito (Javassist) cannot mock this class: " + mockedType
                        + ".\n"
                        + "Most likely it is a private class that is not visible by Funcito");
            } else if (mockedType.isInterface()
                    && !Modifier.isPublic(mockedType.getModifiers())
                    && !Modifier.isProtected(mockedType.getModifiers())) {
                // NOTE: this appears to be a limitation of Javassist, not present in Cglib
                throw new FuncitoException("\n"
                        + "Funcito (Javassist) cannot mock this class: " + mockedType
                        + ".\n"
                        + "Most likely it is an interface with default access that is not visible by Funcito");
            }
            throw new FuncitoException("\n"
                    + "Funcito (Javassist)cannot mock this class: " + mockedType
                    + ".\n"
                    + "Funcito (Javassist) can only mock visible & non-final classes", e);
        }
    }

    private Object createProxy(Class<?> proxyClass, final MethodHandler handler) {
        ProxyObject proxy = (ProxyObject) objenesis.newInstance(proxyClass);
        proxy.setHandler(handler);
//        proxy.setCallbacks(new Callback[] {interceptor, NoOp.INSTANCE});
        return proxy;
    }

    private Class<?>[] prepend(Class<?> first, Class<?>... rest) {
        Class<?>[] all = new Class<?>[rest.length + 1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }

    public static class ClassWithSuperclassToWorkAroundCglibBug {
    }
}
