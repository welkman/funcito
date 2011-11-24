package org.funcito.internal;

import static org.junit.Assert.*;

import org.junit.Test;


public class WrapperType_UT {
	@Test
	public void testGuavaFunction() {
		// test
		assertEquals("Guava Function", WrapperType.GUAVA_FUNCTION.toString());
	}
	
	@Test
	public void testGuavaPredicate() {
		// test
		assertEquals("Guava Predicate", WrapperType.GUAVA_PREDICATE.toString());
	}

	@Test
	public void testFJ_F() {
		// test
		assertEquals("Functional Java F (function)", WrapperType.FJ_F.toString());
	}
	
}
