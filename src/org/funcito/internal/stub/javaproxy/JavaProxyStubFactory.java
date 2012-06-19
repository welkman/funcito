package org.funcito.internal.stub.javaproxy;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.StubFactory;

import java.lang.reflect.Proxy;

public class JavaProxyStubFactory extends StubFactory {

    private JavaProxyInvocationHandler handler = new JavaProxyInvocationHandler();

    @Override
    public <T> T stubImpl(Class<T> clazz, Class<?>... additionalInterfaces) {
        if (!canImposterise(clazz)) {
            throw new FuncitoException("Cannot proxy this class using the Java Proxy Stub Factory because it is not an interface.  If no interface is available for the class, then you will need to include a code-generation library (CGLib or Javassist) in order to be able to proxy this non-interface class.");
        }
        // TODO: add any additional interfaces (i.e., ProxyReturnValue) to support chaining
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
    }

    @Override
    public boolean canImposterise(Class<?> type) {
        return type.isInterface();
    }
}
