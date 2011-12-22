package org.funcito.internal.stub;

import static org.junit.Assert.*;

import org.funcito.internal.stub.StubFactory;
import org.junit.Test;

public class StubFactory_UT {
    @Test
    public void testInstance_CreatesOnlyOne() {
        // test
        StubFactory sf1 = StubFactory.instance();
        StubFactory sf2 = StubFactory.instance();

        assertSame(sf1, sf2);
    }
}
