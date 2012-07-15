package org.funcito.internal.stub;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;
import org.funcito.internal.WrapperType;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNull;

public class ProxyMethodHandler_UT {

    @Test
    public void testGenericReturnTypesAreNotProxyable() throws NoSuchMethodException {
        class X<T> {
            public T t;
            public T returnGenericType() {return this.t;}
        }
        X<CharSequence> xInst = new X<CharSequence>();
        xInst.t = "ABC";
        Method returnGenericType = xInst.getClass().getMethod("returnGenericType");

        new ProxyMethodHandler().handleProxyMethod(xInst, returnGenericType, null);

        InvokableState state = new FuncitoDelegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        Invokable invokable = state.iterator().next();
        // even though CharSequence is an interface (and hence, proxyable), we choose not to proxy the return
        // type for this method because it is defined as a Generic, and hence erasure means that it is Object
        // at run time.  So you can wrap a generic return type method, but *not* chain it.
        assertNull(invokable.getRetVal());
    }
}
