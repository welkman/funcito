package org.funcito.internal.stub.cglib;

import org.funcito.internal.FuncitoDelegate;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Stack;

import static org.junit.Assert.assertNull;

public class CglibMethodInterceptor_UT {

    private CglibMethodInterceptor interceptor = new CglibMethodInterceptor();
    private FuncitoDelegate delegate = new FuncitoDelegate();

    @After
    public void tearDown() {
        try {
            // pop last method pushed, between each test
            delegate.getInvokable(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    @Test
    public void testInvoke_noExceptionForObject() throws Throwable {
        Method objMethod = Stack.class.getDeclaredMethod("pop");

        Object fakeObj = interceptor.intercept(new Stack(), objMethod, null, null);

        assertNull(fakeObj);
    }
}
