package org.funcito.internal.stub.javaproxy;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.stub.javassist.JavassistInvokable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JavaProxyInvocationHandler implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        new FuncitoDelegate().putInvokable(new JavassistInvokable(method, proxy.getClass()));
        return null;
    }
}
