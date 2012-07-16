package org.funcito.internal.stub;

import static org.junit.Assert.*;

import org.junit.Test;

public class StubFactory_UT {
    @Test
    public void testInstance_CreatesOnlyOne() {
        // test
        ProxyFactory pf1 = ProxyFactory.instance();
        ProxyFactory pf2 = ProxyFactory.instance();

        assertSame(pf1, pf2);
    }
}
