package org.funcito.jedi;

import jedi.functional.Command;
import jedi.functional.Filter;
import jedi.functional.Functor;
import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.InvokableState;
import org.funcito.mode.Mode;
import org.funcito.mode.TypedMode;
import org.funcito.mode.NoOp;

import static org.funcito.internal.WrapperType.JEDI_FUNCTOR;
import static org.funcito.internal.WrapperType.JEDI_FILTER;
import static org.funcito.internal.WrapperType.JEDI_COMMAND;
import static org.funcito.internal.WrapperType.JEDI_VOID_COMMAND;

/*
 * Copyright 2011-13 Project Funcito Contributors
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
public class JediDelegate extends FuncitoDelegate {

    /**
     * Delegated version of <code>FuncitoJedi.functorFor(V)</code>
     * @see org.funcito.FuncitoJedi#functorFor(Object)
     */
    public <T,V> Functor<T,V> functorFor(V ignoredRetVal) {
        final InvokableState state = extractInvokableState(JEDI_FUNCTOR);
        return new JediFunctor<T, V>(state, NoOp.NO_OP);
    }

    /**
     * Delegated version of <code>FuncitoJedi.functorFor(V,TypedMode)</code>
     * @see org.funcito.FuncitoJedi#functorFor(Object, org.funcito.mode.TypedMode)
     */
    public <T,V> Functor<T,V> functorFor(V ignoredRetVal, TypedMode<V> mode) {
        final InvokableState state = extractInvokableState(JEDI_FUNCTOR);
        return new JediFunctor<T, V>(state, mode);
    }

    /**
     * Delegated version of <code>FuncitoJedi.functorFor(V,Mode)</code>
     * @see org.funcito.FuncitoJedi#functorFor(Object, org.funcito.mode.Mode)
     */
    public <T,V> Functor<T,V> functorFor(V ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(JEDI_FUNCTOR);
        return new JediFunctor<T, V>(state, mode);
    }

    public <T> Filter<T> filterFor(Boolean ignoredRetVal) {
        final InvokableState state = extractInvokableState(JEDI_FILTER);
        return new JediFilter<T>(state, NoOp.NO_OP);
    }

    public <T> Filter<T> filterFor(Boolean ignoredRetVal, TypedMode<Boolean> mode) {
        final InvokableState state = extractInvokableState(JEDI_FILTER);
        return new JediFilter<T>(state, mode);
    }

    public <T> Filter<T> filterFor(Boolean ignoredRetVal, Mode mode) {
        final InvokableState state = extractInvokableState(JEDI_FILTER);
        return new JediFilter<T>(state, mode);
    }

    public <T> Command<T> commandFor(Object proxiedMethodCall) {
        InvokableState state = extractInvokableState(JEDI_COMMAND);
        return new JediCommand<T>(state, NoOp.NO_OP);
    }

    public <T> Command<T> commandFor(Object proxiedMethodCall, TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(JEDI_COMMAND);
        return new JediCommand<T>(state, mode);
    }

    public <T> Command<T> commandFor(Object proxiedMethodCall, Mode mode) {
        InvokableState state = extractInvokableState(JEDI_COMMAND);
        return new JediCommand<T>(state, mode);
    }

    public <T> T prepareVoid(T t) { return t; }

    public <T> Command<T> voidCommand() {
        InvokableState state = extractInvokableState(JEDI_VOID_COMMAND);
        return new JediCommand<T>(state, NoOp.NO_OP);
    }

    public <T> Command<T> voidCommand(TypedMode<Void> mode) {
        InvokableState state = extractInvokableState(JEDI_VOID_COMMAND);
        return new JediCommand<T>(state, mode);
    }

    public <T> Command<T> voidCommand(Mode mode) {
        InvokableState state = extractInvokableState(JEDI_VOID_COMMAND);
        return new JediCommand<T>(state, mode);
    }
}
