package org.funcito.functorbase;

import org.funcito.FuncitoException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class NullValidatingPredicate_UT extends FunctorBaseTestBase {

    @Rule public ExpectedException expected = ExpectedException.none();

    private class ExampleApiFunctorClass {
        public Boolean apply() { return true; }
    }
    private static Method anyMethod = null;
    static {
        try {
            anyMethod = ExampleApiFunctorClass.class.getMethod("apply");
        } catch (NoSuchMethodException e){}
    }

    // This is what differentiates execution of inherited tests from parent test class FunctorBaseTestBase
    @Override
    protected <T, V> FunctorBase<T, V> makeFunctorForTest() {
        // for FunctorBaseTestBase tests, I just need non-null values for Class/Method parms
        return (FunctorBase<T, V>)new NullValidatingPredicate(getState(), ExampleApiFunctorClass.class, anyMethod);
    }

    private final BoolThing CALLS_TO_BOOL_THING = delegate.callsTo(BoolThing.class);

    private class BoolThing {
        private Boolean val = null;
        private BoolThing child = null;
        BoolThing(Boolean val) { this.val = val; }
        Boolean returnVal() { return val; }
        BoolThing child() { return child; }
    }

    @Test
    public void testPrimitivePredicate_DoesNotDoSafeNavigationWithMiddleNulls() {
        CALLS_TO_BOOL_THING.child().returnVal();
        NullValidatingPredicate predicate = new NullValidatingPredicate(getState(), ExampleApiFunctorClass.class, anyMethod);

        expected.expect(FuncitoException.class);
        expected.expectMessage("NullPointerException");
        predicate.applyImpl(new BoolThing(true));
    }

    @Test
    public void testPrimitivePredicate_validatesTailReturnValueNotNull() {
        CALLS_TO_BOOL_THING.child();
        NullValidatingPredicate predicate = new NullValidatingPredicate(getState(), ExampleApiFunctorClass.class, anyMethod);

        expected.expect(FuncitoException.class);
        expected.expectMessage("ExampleApiFunctorClass returned a null Boolean");
        expected.expectMessage("consider the alternate method: " + anyMethod.toGenericString());
        predicate.applyImpl(new BoolThing(true));
    }
}
