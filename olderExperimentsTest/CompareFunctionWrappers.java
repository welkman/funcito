import static ch.lambdaj.Lambda.closure;
import static ch.lambdaj.Lambda.of;
import static info.piwai.funkyjfunctional.guava.FunkyGuava.*;
import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

import ch.lambdaj.function.closure.Closure1;
import com.google.common.base.Function;
import info.piwai.funkyjfunctional.guava.Func;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompareFunctionWrappers {

    private static class StringThing {
        protected String myString;
        public StringThing(String myString) { this.myString = myString; }
        public Integer size() { return myString.length(); }
        // NOTE: I could have returned the primitive itself, but that means that all Function solutions
        // would have the overhead of autoboxing, making the comparison to non-functional unfair
    }


    // Example 1 with FunkyJFunctional
    static private class F extends Func<StringThing, Integer> {{ out = in.size(); }}
    static private Function<StringThing, Integer> funkyFunc = withFunc(F.class);

    private static class Fx2 extends Func<StringThing, Integer> {{ out = in.size()*2; }}
    private static Function<StringThing, Integer> funkyFuncX2 = withFunc(Fx2.class);


    // Example 2 with my Mockito/mock-based type
    private StringThing mockitoStub = mockStubs.MethodWrapper.stubbed(StringThing.class);
    private Function<StringThing, Integer> mockitoFunc = mockStubs.MethodWrapper.makeFunc(mockitoStub.size());
    private Function<StringThing, Integer> mockitoFuncX2 = mockStubs.MethodWrapper.makeFunc(mockitoStub.size() * 2);


    // Example 3 with normal manual definition of Function
    private static Function<StringThing, Integer> manual = new Function<StringThing, Integer>() {
          public Integer apply(StringThing arg0) { return arg0.size(); }
    };
    private static Function<StringThing, Integer> manualX2 = new Function<StringThing, Integer>() {
        public Integer apply(StringThing arg0) { return arg0.size()*2; }
    };


    // Example 4 Funcito (CGLib)
    private static Function<StringThing, Integer> funcitoFunc = Guava.functionFor(Guava.callsTo(StringThing.class).size());

    private static long timeManual = 0L;

    @BeforeClass
    public static void setupOnce() {
        long timeDirect = invoke();
        System.out.println("direct = " + timeDirect);
        timeManual = apply(manual);
        System.out.println("manual = " + timeManual);
    }

    @Test
    public void testFuncitoSpeed() {
        long funcitoTime = apply(funcitoFunc);
        System.out.println("Funcito (CGLib) = " + funcitoTime);
        assertTrue(10L * timeManual > funcitoTime);
    }

    @Test
    public void testMockitoSpeed() {
        long mockitoTime = apply(mockitoFunc);
        System.out.println("MyMockito-based = " + mockitoTime);
        assertTrue(10L * timeManual > mockitoTime);
    }

    @Test
    public void testFunkyJFunctionalSpeed() {
        long funkyTime = apply(funkyFunc);
        System.out.println("FunkyJFunctional = " + funkyTime);
        assertTrue(10L * timeManual > funkyTime);
    }

    @Test
    public void testLambdaJ_ClassWithoutNoArgCtor() {
        // This does not run because StringThing does not have no-arg constructor
        Closure1<StringThing> lambdaClosure = closure(StringThing.class); {of(StringThing.class).size();}
    }

    public static class StringThing2 extends StringThing {
        // include a no-arg ctor
        public StringThing2() {super(""); myString = "123456";}
    }

    @Test
    public void testLambdaJSpeed() {
        Closure1<StringThing2> lambdaClosure = closure(StringThing2.class); {of(StringThing2.class).size();}
        long lambdaJTime = applyLambdaJ(lambdaClosure);
        System.out.println("LambdaJ native = " + lambdaJTime);
        assertTrue(10L * timeManual > lambdaJTime);
    }

    class OtherThing { // does NOT extend BooleanThing or implement common.common interface, but they look identical
        private String myString;
        public OtherThing(String myString) { this.myString = myString; }
        public Integer size() { return myString.length(); }
    }

    @Test
    public void testMockito_detectMismatchOfSourceType() {
        OtherThing otherThingStub = mockStubs.MethodWrapper.stubbed(OtherThing.class);
        try {
            Function<StringThing, Integer> func = mockStubs.MethodWrapper.makeFunc(otherThingStub.size());
            fail("Should not have allowed wrapping an OtherThing method as a StringThing function");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testLambdaJ_detectMismatchOfSourceType() {
        class OtherThing2 extends OtherThing { // needed a class with no-arg ctor
            public OtherThing2(String myString) {super("123456");}
        }
        try {
            Closure1<StringThing> lambdaClosure = closure(StringThing.class); {of(OtherThing2.class).size();}
            fail("Should not have allowed wrapping an OtherThing2 method as a StringThing Closure");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testFunkyJFunctional_detectMismatchOfSourceType() {
        class F3 extends Func<OtherThing, Integer> {{ out = in.size(); }}
        try {
            // This has static (compile-time) safety, so test passes by virtue of static source type safety
//            Function<BooleanThing, Integer> funky2 = withFunc(F3.class);
//            fail("Should not have allowed this");
        } catch (Exception e) {
            // Happy Path
        }
    }

    //----------------- HELPER METHODS ------------------------------

    private long applyLambdaJ(Closure1<StringThing2> lambdaClosure) {
        StringThing2 stringThing2 = new StringThing2();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            lambdaClosure.apply(stringThing2);
        }
        long duration = System.currentTimeMillis() - startTime;
        return duration;
    }

    private static long apply(Function<StringThing, Integer> function) {
        StringThing stringThing = new StringThing("1234567890ABCDEF");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
              function.apply(stringThing);
        }
        long duration = System.currentTimeMillis() - startTime;
        return duration;
    }
    private static long invoke() {
        StringThing stringThing = new StringThing("1234567890ABCDEF");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
              stringThing.size();
        }
        long duration = System.currentTimeMillis() - startTime;
        return duration;
    }
}
