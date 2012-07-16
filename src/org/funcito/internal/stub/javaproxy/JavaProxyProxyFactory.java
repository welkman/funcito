package org.funcito.internal.stub.javaproxy;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.ProxyFactory;

import java.lang.reflect.Proxy;

public class JavaProxyProxyFactory extends ProxyFactory {

    private JavaProxyInvocationHandler handler = new JavaProxyInvocationHandler();

    @Override
    public <T> T proxyImpl(Class<T> clazz, Class<?>... additionalInterfaces) {
        if (!canImposterise(clazz)) {
            throw new FuncitoException("Cannot proxy this class using the JavaProxyProxyFactory because it is not an interface.  If no interface is available for the class, then you will need to include a code-generation library (CGLib or Javassist) in order to be able to proxy this non-interface class.");
        }
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
    }

    @Override
    public boolean canImposterise(Class<?> type) {
        return type.isInterface();
    }
}
