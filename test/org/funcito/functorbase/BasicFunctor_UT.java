package org.funcito.functorbase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PLEASE NOTICE: that this class extends abstract FunctorBaseTestBase, and inherits all tests from it
 */
public class BasicFunctor_UT extends FunctorBaseTestBase {

    @Override
    protected <T,V> AbstractFunctorBase<T, V> makeFunctorForTest() {
        return new BasicFunctor<T, V>(getState());
    }

    @Test
    public void test_NonPrimitiveArrayRetType() {
        class NonPrimArrayRet {
            public Object[] getVal() { return new Object[] {"ABC", Integer.MAX_VALUE}; }
        }
        delegate.callsTo(NonPrimArrayRet.class).getVal();
        FunctorBase<NonPrimArrayRet, Object[]> wrapperIntFunc = makeFunctorForTest();

        assertEquals(2, wrapperIntFunc.applyImpl(new NonPrimArrayRet()).length);
    }

    @Test
    public void test_PrimitiveArrayRetType() {
        class PrimArrayRet {
            public int[] getVal() { return new int[] {1,2,3}; }
        }
        delegate.callsTo(PrimArrayRet.class).getVal();
        FunctorBase<PrimArrayRet, int[]> wrapperIntFunc = makeFunctorForTest();

        assertEquals(3, wrapperIntFunc.applyImpl(new PrimArrayRet()).length);
    }
}
