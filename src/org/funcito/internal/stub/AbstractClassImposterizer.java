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

package org.funcito.internal.stub;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Thanks to Mockito guys (and indirectly, jMock) for this handy class that wraps all the cglib magic.
 * The orginal class has been divided between here and CglibImposterizer (and mimicked in JavassistImposterizer)
 */
abstract public class AbstractClassImposterizer {

    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
    }

    protected void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }

    protected Class<?>[] prepend(Class<?> first, Class<?>... rest) {
        Class<?>[] all = new Class<?>[rest.length + 1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }
}
