package org.funcito.internal;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.javassist.JavassistInvokable;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertSame;

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
public class FuncitoDelegate_UT {

    private FuncitoDelegate delegate = new FuncitoDelegate();

    @Test
    public void threadSafety() throws Exception {
        Method method1 = MyClass.class.getDeclaredMethod("getString1");
        Method method2 = MyClass.class.getDeclaredMethod("getString2");
        final JavassistInvokable<MyClass,String> invokable1 = new JavassistInvokable<MyClass,String>(method1);
        final JavassistInvokable<MyClass,String> invokable2 = new JavassistInvokable<MyClass,String>(method2);
        final Semaphore lock1 = new Semaphore(1);
        final Semaphore lock2 = new Semaphore(1);
        final Invokable<?,?>[] ret2 = new Invokable[1];
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    delegate.putInvokable(invokable2);
                    lock1.acquire();
                    lock2.release();
                    ret2[0] = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
                    lock1.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                } catch (FuncitoException e) {
                    lock2.release();
                    lock1.release();
                    throw e;
                }
            }
        };
        delegate.putInvokable(invokable1);
        lock2.acquire();
        Thread t = new Thread(runnable);
        t.start();
        while (lock2.availablePermits()==0) {}
        Invokable ret1 = delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        assertSame(invokable1, ret1);
        lock1.acquire();
        assertSame(invokable2, ret2[0]);
    }

    private class MyClass{
        public String getString1() { return "string1"; }
        public String getString2() { return "string2"; }
    }
}
