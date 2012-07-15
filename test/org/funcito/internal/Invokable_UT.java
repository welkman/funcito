package org.funcito.internal;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.javassist.JavassistImposterizer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
public class Invokable_UT {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    @SuppressWarnings("unchecked")
    public void testInvoke_catchesTypeErasureAtRuntime() throws Throwable {
        class Thing1 {
            public String getVal() { return "abc"; }
        }
        class Thing2 {
            public String getVal() { return "abc"; }
        } // same signature as Thing1.getVal() but not a subclass

        Thing1 thing1Mock = JavassistImposterizer.INSTANCE.imposterise(new Handler<String>("A"), Thing1.class);
        thing1Mock.getVal(); // mock call intercepted and MethodProxy extracted
        Method thing1Method = Thing1.class.getMethod("getVal");
        Invokable invokableForThing1 = new Invokable<Thing1, String>(thing1Method, new Thing1(), "ABC");

        // prove that above test setup works properly with the proper type
        assertEquals("abc", invokableForThing1.invoke(new Thing1()));

        // Type erasure means we could queue up calls to other than Thing1
        // Try invoking on wrong type
        thrown.expect(FuncitoException.class);
        thrown.expectMessage("You attempted to invoke");
        invokableForThing1.invoke(new Thing2());
    }

    @Test
    public void testInvoke_rethrowsThrowableInternalToMethodAsFuncitoException() throws Throwable {
        class MyThrowable extends Throwable{}
        class ThrowsThrowable {
            public Integer doStuff() throws MyThrowable { throw new MyThrowable(); }
        }
        ThrowsThrowable ttObj = JavassistImposterizer.INSTANCE.imposterise(new Handler<Integer>(1), ThrowsThrowable.class);
        ttObj.doStuff(); // mock call intercepted and Method registered, without throwing the exception yet
        Method method = ThrowsThrowable.class.getMethod("doStuff");
        Invokable<ThrowsThrowable, Integer> invokable = new Invokable<ThrowsThrowable, Integer>(method, new ThrowsThrowable(), 1);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Caught throwable ");
        thrown.expectMessage("MyThrowable");
        invokable.invoke(new ThrowsThrowable());
    }


    @Test
    public void testInvoke_internalClassCastExceptionDifferentFromErasureError() throws NoSuchMethodException {
        class ThrowsCastException{
            public Integer doStuff() {
                Object o = Boolean.TRUE;
                return (Integer)o; // should throw ClassCastException
            }
        }
        ThrowsCastException tceObj = JavassistImposterizer.INSTANCE.imposterise(new Handler<Integer>(1), ThrowsCastException.class);
        tceObj.doStuff(); // calling this on the imposter registers the Method without throwing the exception
        Method m = ThrowsCastException.class.getMethod("doStuff");
        Invokable<ThrowsCastException, Integer> invokable = new Invokable<ThrowsCastException, Integer>(m, new ThrowsCastException(), 1);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Caught throwable ");
        invokable.invoke(new ThrowsCastException());
    }

    class Handler<T> implements MethodHandler {
        T retVal;
        public Handler(T retVal) {
            this.retVal = retVal;
        }
        public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
            return retVal;
        }
    }
}
