package org.funcito.internal.stub.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.funcito.FuncitoException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

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
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testInvoke_catchesTypeErasureAtRuntime() throws Throwable {
        class Thing1 {
            public String getVal() { return "abc"; }
        }
        class Thing2 {
            public String getVal() { return "abc"; }
        } // same signature but not a subclass
        Thing1 thing1Mock = CglibImposterizer.INSTANCE.imposterise(new Interceptor(), Thing1.class);
        thing1Mock.getVal(); // mock call intercepted and MethodProxy/Method extracted

        // Type erasure means we could queue up calls to other than Thing1
        CglibInvokable invokableForThing1 = new CglibInvokable<Thing1, String>(proxy[0], Thing1.class);

        // preassert that above test setup works properly with the proper type
        assertEquals("abc", invokableForThing1.invoke(new Thing1(), (Object[]) null));

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("You attempted to invoke");
            // Try invoking on wrong type
        invokableForThing1.invoke(new Thing2(), (Object[]) null);
    }

    @Test
    public void testInvoke_rethrowsThrowableInternalToMethodAsFuncitoException() throws Throwable {
        class MyThrowable extends Throwable{};
        class ThrowsThrowable {
            Integer doStuff() throws Throwable { throw new MyThrowable(); }
        }
        ThrowsThrowable ttObj = CglibImposterizer.INSTANCE.imposterise(new Interceptor(), ThrowsThrowable.class);
        ttObj.doStuff(); // calling this on the imposter registers the Method/MethodProxy without throwing the exception
        CglibInvokable invokable = new CglibInvokable<ThrowsThrowable, String>(proxy[0], ThrowsThrowable.class);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Caught throwable ");
        thrown.expectMessage("MyThrowable");
        invokable.invoke(new ThrowsThrowable());
    }

    @Test
    public void testInvoke_internalClassCastExceptionDifferentFromErasureError() throws NoSuchMethodException {
        class ThrowsCastException{
            Integer doStuff() {
                Boolean b = true;
                Object o = b;
                return (Integer)o; // should throw ClassCastException
            }
        }
        ThrowsCastException tceObj = CglibImposterizer.INSTANCE.imposterise(new Interceptor(), ThrowsCastException.class);
        tceObj.doStuff(); // calling this on the imposter registers the Method/MethodProxy without throwing the exception
        CglibInvokable invokable = new CglibInvokable<ThrowsCastException, String>(proxy[0], ThrowsCastException.class);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Caught throwable ");
        invokable.invoke(new ThrowsCastException());
    }

    final MethodProxy[] proxy = new MethodProxy[1];
    class Interceptor implements MethodInterceptor {
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            proxy[0] = methodProxy;
            return null;
        }
    }
}
