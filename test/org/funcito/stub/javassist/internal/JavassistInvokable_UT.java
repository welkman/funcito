package org.funcito.stub.javassist.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

import org.funcito.FuncitoException;
import org.funcito.stub.javassist.internal.JavassistImposterizer;
import org.junit.Test;

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
public class JavassistInvokable_UT {

    @Test
    public void testInvoke_catchesTypeErasureAtRuntime() throws Throwable {
        final Method[] methodCalled = new Method[1];
        class Interceptor implements MethodHandler {
            public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
                methodCalled[0] = method;
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }
        class Thing1 {
            public String getVal() { return "abc"; }
        }
        class Thing2 {
            public String getVal() { return "abc"; }
        } // same signature but not a subclass

        Thing1 thing1Proxy = JavassistImposterizer.INSTANCE.imposterise(new Interceptor(), Thing1.class);
        thing1Proxy.getVal(); // proxy call intercepted and MethodProxy extracted

        // Type erasure means we could queue up calls to other than Thing1
        JavassistInvokable invokableForThing1 = new JavassistInvokable<Thing1, String>(methodCalled[0]);

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
