package org.funcito.internal.stub.javaproxy;

import org.funcito.google.guava.common.base.Defaults;
import org.funcito.internal.FuncitoDelegate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JavaProxyInvocationHandler implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        new FuncitoDelegate().putInvokable(new JavaproxyInvokable(method, proxy.getClass()));

        return Defaults.defaultValue(method.getReturnType());
    }
}
