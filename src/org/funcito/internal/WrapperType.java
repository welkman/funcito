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
package org.funcito.internal;

public enum WrapperType {
    GUAVA_FUNCTION("Guava Function"), 
    GUAVA_PREDICATE("Guava Predicate"),

    FJ_F("Functional Java F (function)"),
    FJ_EFFECT("Functional Java Effect"),
    FJ_VOID_EFFECT("Functional Java Effect (returning void)"),

    JEDI_FUNCTOR("Jedi Functor"),
    JEDI_FILTER("Jedi Filter"),
    JEDI_COMMAND("Jedi Command"),
    JEDI_VOID_COMMAND("Jedi Command (returning void)"),

    PLAY2_FUNCTION("Play! Framework 2 Function"),
    PLAY2_CALLBACK("Play! Framework 2 Callback"),
    PLAY2_VOID_CALLBACK("Play! Framework 2 Callback"),

    COLLECTGEN_TRANSFORMER("Collections-Generic Transformer"),
    COLLECTGEN_PREDICATE("Collections-Generic Predicate"),
    COLLECTGEN_CLOSURE("Collections-Generic Closure"),
    COLLECTGEN_VOID_CLOSURE("Collections-Generic Closure (returning void)")
    ;

    private final String name;
    
    private WrapperType(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name;
    }
}
