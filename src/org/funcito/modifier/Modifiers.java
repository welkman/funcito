/*
 * Copyright 2013 Project Funcito Contributors
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
package org.funcito.modifier;

// TODO: Javadoc
public class Modifiers {

    // TODO: Javadoc
    public static UntypedModifier noOp() {
        return NoOp.NO_OP;
    }

    // TODO: Javadoc
    public static <T,V> Modifier<T,V> safeNav(V v) {
        return new SafeNav<T,V>(v);
    }

    // TODO: Javadoc
    public static UntypedModifier safeNav() {
        return UntypedSafeNav.SAFE_NAV;
    }

    // maybe *also* make defaultTrue()/defaultFalse(), or nullIsTrue()/nullIsFalse()
    public static <T> UntypedModifier defaultBool(boolean defaultForNull) {
        return new PrimitiveBoolDefault<T>(defaultForNull);
    }

}
