package org.funcito.functorbase;

import java.lang.reflect.Method;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class NullValidatingPredicate_UT extends FunctorBaseTestBase {

    // This is what differentiates execution of inherited tests from parent test class FunctorBaseTestBase
    @Override
    protected <T, V> FunctorBase<T, V> makeFunctorForTest() {
        // for FunctorBaseTestBase tests, I just need non-null values for Class/Method parms
        Method method = null;
        try {
            method = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e){}
        return (FunctorBase<T, V>)new NullValidatingPredicate(getState(), Object.class, method);
    }

    // TODO: more tests
}
