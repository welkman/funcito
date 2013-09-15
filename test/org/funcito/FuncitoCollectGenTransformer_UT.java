package org.funcito;

import org.apache.commons.collections15.Transformer;
import org.funcito.internal.WrapperType;
import org.junit.After;
import org.junit.Test;

import static org.funcito.FuncitoCollectGen.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FuncitoCollectGenTransformer_UT {

    private StringThing CALLS_TO_STRING_THING = callsTo(StringThing.class);

    private class StringThing {
        protected String myString;

        public StringThing(String myString) { this.myString = myString; }
        public int size() { return myString.length(); }
        public String toString() { return myString; }
    }

    @After
    public void tearDown() {
        try {
            // cleanup aftermath of failed tests
            delegate().extractInvokableState(WrapperType.GUAVA_FUNCTION);
        } catch (Throwable t) {}
    }

    @Test
    public void testTransformerFor_AssignToTransformerWithMatchingTypes() { // I.e. vanilla happy path
        Transformer<StringThing, Integer> superTypeRet = transformerFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.transform(new StringThing("ABC")).intValue());
    }

    @Test
    public void testTransformerFor_AssignToTransformerWithSourceSuperType() {
        Transformer<Object, Integer> superTypeRet = transformerFor(CALLS_TO_STRING_THING.size());
        assertEquals(3, superTypeRet.transform(new StringThing("ABC")).intValue());
    }

    @Test
    public void testTransformerFor_AssignToFuncWithTargetSuperType() {
        Transformer<StringThing, ? extends Number> superType = transformerFor(CALLS_TO_STRING_THING.size());
        StringThing thing = new StringThing("123456");
        Number n = superType.transform(thing);
        assertEquals(6, n);
    }

    @Test
    public void testTransformerFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() { return 123; }
        }
        class AnyOldClass{}
        Transformer<Generic<?>, Integer> stringFunc = transformerFor(callsTo(Generic.class).getVal());
        Generic<AnyOldClass> anyOldGeneric = new Generic<AnyOldClass>();

        assertEquals(123, stringFunc.transform(anyOldGeneric).intValue());
    }

    @Test
    public void testTransformerFor_ExpressionsWithOperatorsAreUnsupported() {
        Transformer<StringThing,String> pluralFunc = transformerFor(CALLS_TO_STRING_THING.toString() + "s");
        StringThing dog = new StringThing("dog");

        // NOTE: this test is a test that proves and documents a limitation of Funcito
        assertFalse("dogs".equals( pluralFunc.transform(dog)));
        assertEquals("dog", pluralFunc.transform(dog));
    }
}

