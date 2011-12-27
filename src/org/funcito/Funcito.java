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
package org.funcito;

import org.funcito.functionaljava.FJDelegate;
import org.funcito.guava.GuavaDelegate;
import org.funcito.jedi.JediDelegate;

/**
 * This class is a public entry point into Funcito for the supported functional framework APIs.  Most users will probably
 * prefer to use static imports from the particular API being used, such as {@link FuncitoGuava}, {@link FuncitoFJ} (Functional Java),
 * or {@link FuncitoJedi}, because the usage is more terse.  But for rare cases when multiple functional frameworks are
 * being used within the same class, the static methods within this class can be used to differentiate between their usages.
 * Example:
 * <p>
 * <code>
 *     import org.funcito.Funcito.*;<br/>
 *     <br/>
 *     MyClass guavaStub function = guava().callsTo(MyClass.class);<br/>
 *     MyClass fjStub f = fj().callsTo(MyClass.class);<br/>
 *     <br/>
 *     Function<MyClass,RetType> = guava().functionFor(guavaStub.someMethod());<br/>
 *     F<MyClass,RetType> = fj().fFor(fjStub.someMethod());<br/>
 * </code>
 * <p>
 * or calls may be made inline without problem, though it may be less readable to some:
 * <p>
 * <code>
 *     Function<MyClass,RetType> function = guava().functionFor( guava().callsTo(MyClass.class).someMethod());<br/>
 * </code>
 * @see FuncitoGuava
 * @see FuncitoFJ
 * @see FuncitoJedi
 */
public class Funcito {

    public static GuavaDelegate guava() { return FuncitoGuava.delegate(); }
    public static FJDelegate fj() { return FuncitoFJ.delegate(); }
    public static JediDelegate jedi() { return FuncitoJedi.delegate(); }
}
