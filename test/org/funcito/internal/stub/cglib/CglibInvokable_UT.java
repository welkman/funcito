package org.funcito.internal.stub.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.funcito.FuncitoException;
import org.funcito.internal.stub.cglib.CglibImposterizer;
import org.funcito.internal.stub.cglib.CglibInvokable;
import org.junit.Test;

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

    @Test
    public void testInvoke_catchesTypeErasureAtRuntime() throws Throwable {
        final MethodProxy[] proxy = new MethodProxy[1];
        class Interceptor implements MethodInterceptor {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                proxy[0] = methodProxy;
                return null;
            }
        }
        class Thing1 {
            public String getVal() { return "abc"; }
        }
        class Thing2 {
            public String getVal() { return "abc"; }
        } // same signature but not a subclass

        Thing1 thing1Proxy = CglibImposterizer.INSTANCE.imposterise(new Interceptor(), Thing1.class);
        thing1Proxy.getVal(); // proxy call intercepted and MethodProxy extracted

        // Type erasure means we could queue up calls to other than Thing1
        CglibInvokable invokableForThing1 = new CglibInvokable<Thing1, String>(proxy[0]);

        // prove that above test setup works properly with the proper type
        assertEquals("abc", invokableForThing1.invoke(new Thing1(), (Object[]) null));
        try {
            // Try invoking on wrong type
            invokableForThing1.invoke(new Thing2(), (Object[]) null);
            fail("Failed to catch type mismatch");
        } catch (FuncitoException e) {
            // happy
            assertTrue(e.getMessage().contains("ClassCast"));
        }
    }
}
