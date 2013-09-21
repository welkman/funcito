package org.funcito.functorbase;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class PrimitivePredicate_UT extends FunctorBaseTestBase {

    @Override
    protected <T, V> FunctorBase<T, V> makeFunctorForTest() {
        return (FunctorBase<T, V>) new PrimitivePredicate<T>(getState(), true);
    }

    // TODO: add/move more tests, from FuncitoGuavaPredicate_UT (and collectgen)
}
