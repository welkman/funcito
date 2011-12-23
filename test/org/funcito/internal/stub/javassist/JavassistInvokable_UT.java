package org.funcito.internal.stub.javassist;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

import org.funcito.FuncitoException;
import org.junit.Ignore;
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
public class JavassistInvokable_UT {

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
        Method thing1Method = Thing1.class.getMethod("getVal");

        Thing1 thing1Mock = JavassistImposterizer.INSTANCE.imposterise(new Handler(), Thing1.class);
        thing1Mock.getVal(); // mock call intercepted and MethodProxy extracted

        // Type erasure means we could queue up calls to other than Thing1
        JavassistInvokable invokableForThing1 = new JavassistInvokable<Thing1, String>(thing1Method);

        // prove that above test setup works properly with the proper type
        assertEquals("abc", invokableForThing1.invoke(new Thing1(), (Object[]) null));

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("You attempted to invoke");
        // Try invoking on wrong type
        invokableForThing1.invoke(new Thing2(), (Object[]) null);
    }

    @Ignore("still working on this functionality")
    @Test
    public void testInvoke_rethrowsThrowableInternalToMethodAsFuncitoException() throws Throwable {
        class MyThrowable extends Throwable{};
        class ThrowsThrowable {
            public Integer doStuff() throws MyThrowable { throw new MyThrowable(); }
        }
        Method method = ThrowsThrowable.class.getMethod("doStuff");

        ThrowsThrowable ttObj = JavassistImposterizer.INSTANCE.imposterise(new Handler(), ThrowsThrowable.class);
        ttObj.doStuff(); // mock call intercepted and MethodProxy extracted

        JavassistInvokable invokable = new JavassistInvokable<ThrowsThrowable, String>(method);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("Caught throwable ");
        thrown.expectMessage("MyThrowable");
        invokable.invoke(new ThrowsThrowable());
    }

    class Handler implements MethodHandler {
        public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
            return null;
        }
    }
}
