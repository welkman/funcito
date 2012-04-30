package org.funcito.internal.stub.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import org.funcito.FuncitoException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
public class CglibInvokable_UT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MethodInterceptor interceptor = mock(MethodInterceptor.class);

    @Test
    @SuppressWarnings("unchecked")
    public void testInvoke_catchesTypeErasureAtRuntime() throws Throwable {
        class Thing1 {
            public String getVal() { return ""; }
        }
        class Thing2 {
            public String getVal() { return ""; }
        } // same signature as Thing1.getVal(), but Thing2 does not extend Thing1

        Thing1 thing1Mock = CglibImposterizer.INSTANCE.imposterise(interceptor, Thing1.class);
        thing1Mock.getVal(); // mock call intercepted and Method extracted
        Method thing1Method = Thing1.class.getMethod("getVal");
        CglibInvokable invokableForThing1 = new CglibInvokable<Thing1, String>(null, Thing1.class, thing1Method);

        // preassert that above test setup works ok with the proper type
        assertEquals("abc", invokableForThing1.invoke(new Thing1(), (Object[]) null));

        // Type erasure means we could queue up calls to other than Thing1.
        // Try invoking on wrong type
        thrown.expect(FuncitoException.class);
        thrown.expectMessage("You attempted to invoke");
        invokableForThing1.invoke(new Thing2(), (Object[]) null);
    }

    @Test
    public void testInvoke_rethrowsThrowableInternalToMethodAsFuncitoException() throws Throwable {
        class MyThrowable extends Throwable{}
        class ThrowsThrowable {
            public Integer doStuff() throws Throwable { throw new MyThrowable(); }
        }
        ThrowsThrowable ttObj = CglibImposterizer.INSTANCE.imposterise(interceptor, ThrowsThrowable.class);
        ttObj.doStuff(); // mock call intercepted and Method registered, without throwing the exception yet
        Method method = ThrowsThrowable.class.getMethod("doStuff");
        CglibInvokable<ThrowsThrowable, String> invokable = new CglibInvokable<ThrowsThrowable, String>(null, ThrowsThrowable.class, method);

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
        ThrowsCastException tceObj = CglibImposterizer.INSTANCE.imposterise(interceptor, ThrowsCastException.class);
        tceObj.doStuff(); // calling this on the imposter registers the Method without throwing the exception
        Method m = ThrowsCastException.class.getMethod("doStuff");
        CglibInvokable<ThrowsCastException, String> invokable = new CglibInvokable<ThrowsCastException, String>(null, ThrowsCastException.class, m);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Caught throwable ");
        invokable.invoke(new ThrowsCastException());
    }

}
