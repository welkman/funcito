import static ch.lambdaj.Lambda.closure;
import static ch.lambdaj.Lambda.of;
import static info.piwai.funkyjfunctional.guava.FunkyGuava.*;
import static org.funcito.Funcito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ch.lambdaj.function.closure.Closure1;
import org.funcito.Funcito;
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
    static class F extends Func<StringThing, Integer> {{ out = in.size(); }}
    static Function<StringThing, Integer> funky = withFunc(F.class);

    static class Fx2 extends Func<StringThing, Integer> {{ out = in.size()*2; }}
    static Function<StringThing, Integer> funky2 = withFunc(Fx2.class);


    // Example 2 with my Mockito/mock-based type
    StringThing mockyStub = mockStubs.MethodWrapper.stubbed(StringThing.class);
    Function<StringThing, Integer> mocky = mockStubs.MethodWrapper.makeFunc(mockyStub.size());
    Function<StringThing, Integer> mockyX2 = mockStubs.MethodWrapper.makeFunc(mockyStub.size() * 2);


    // Example 3 with normal manual definition of Function
    static Function<StringThing, Integer> manual = new Function<StringThing, Integer>() {
          public Integer apply(StringThing arg0) { return arg0.size(); }
    };

    static Function<StringThing, Integer> manualX2 = new Function<StringThing, Integer>() {
        public Integer apply(StringThing arg0) { return arg0.size()*2; }
    };


    // Example 4 Funcito (CGLib)
    static StringThing cglibStringThingStub = stub(StringThing.class);
    static Function<StringThing, Integer> cglibbyX = functionFor(cglibStringThingStub.size());
    // Below compiles but doesn't behave as expected (i.e., unlike FunkyJFunctional)
    // Currently it doesn't run at all, due to null return value somewhere?
//    static Function<StringThing, Integer> cglibby2 = functionFor(cglibStringThingStub.size() * 2);

    // alternative shortened 1-line notation
    static Function<StringThing, Integer> cglibby = functionFor(callsTo(StringThing.class).size());
    // alternate possible API???: makeFunc(forCallTo(...)  makeFunc(onCalls(...)  makeFuncFrom(stubbedCall

    private static long timeManny = 0L;
    @BeforeClass
    public static void setupOnce() {
        long timeDirect = invoke();
        System.out.println("direct = " + timeDirect);
        timeManny = apply(manual);
        System.out.println("manual = " + timeManny);
    }

    @Test
    public void testLibbySpeed() {
        long libbyTime = apply(cglibby);
        System.out.println("Funcito (CGLib) = " + libbyTime);
        assertTrue(10L * timeManny > libbyTime);
    }

    @Test
    public void testMockySpeed() {
        long mockyTime = apply(mocky);
        System.out.println("MyMockito-based = " + mockyTime);
        assertTrue(10L * timeManny > mockyTime);
    }

    @Test
    public void testFunkySpeed() {
        long funkyTime = apply(funky);
        System.out.println("FunkyJFunctional = " + funkyTime);
        assertTrue(10L * timeManny > funkyTime);
    }

    @Test
    public void testLammy_ClassWithoutNoArgCtor() {
        // This does not run because StringThing does not have no-arg constructor
        Closure1<StringThing> lammy = closure(StringThing.class); {of(StringThing.class).size();}
    }

    public static class StringThing2 extends StringThing {
        // include a no-arg ctor
        public StringThing2() {super(""); myString = "123456";}
    }

    @Test
    public void testLammySpeed() {
        Closure1<StringThing2> lammy = closure(StringThing2.class); {of(StringThing2.class).size();}
        long lammyTime = applyLamdaJ(lammy);
        System.out.println("LambdaJ native = " + lammyTime);
        assertTrue(10L * timeManny > lammyTime);
    }

    @Test
    public void testLibby_validateImproperStubCallsOutsideOfWrap() {
        cglibStringThingStub.size(); // should detect calling stubbedCallsTo outside of a wrap call
        try {
            Function<StringThing,Integer> attempt = Funcito.functionFor(cglibStringThingStub.size());
            fail("Should not have succeeded");
        } catch (Exception e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }

    class OtherThing { // does NOT extend BooleanThing or implement common interface, but they look identical
        private String myString;
        public OtherThing(String myString) { this.myString = myString; }
        public Integer size() { return myString.length(); }
    }

    @Test
    public void testLibby_detectMismatchOfSourceType() {
        OtherThing otherThingStub = Funcito.stub(OtherThing.class);
        try {
            Function<StringThing, Integer> func = Funcito.functionFor(otherThingStub.size());
            fail("Should not have allowed wrapping an OtherThing method as a StringThing function");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testLibby_assignToFuncWithSourceSuperType() {
        Function<StringThing, Integer> superType;
        superType = functionFor(callsTo(StringThing2.class).size());
    }

    @Test
    public void testLibby_assignToFuncWithTargetSuperType() {
        Function<StringThing,? extends Number> superType = functionFor(callsTo(StringThing.class).size());
        StringThing thing = new StringThing("123456");
        Number n = superType.apply(thing);
        assertEquals(6,n);
    }

    @Test
    public void testMocky_detectMismatchOfSourceType() {
        OtherThing otherThingStub = mockStubs.MethodWrapper.stubbed(OtherThing.class);
        try {
            Function<StringThing, Integer> func = mockStubs.MethodWrapper.makeFunc(otherThingStub.size());
            fail("Should not have allowed wrapping an OtherThing method as a StringThing function");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testLammy_detectMismatchOfSourceType() {
        class OtherThing2 extends OtherThing { // needed a class with no-arg ctor
            public OtherThing2(String myString) {super("123456");}
        }
        try {
            Closure1<StringThing> lammy = closure(StringThing.class); {of(OtherThing2.class).size();}
            fail("Should not have allowed wrapping an OtherThing2 method as a StringThing Closure");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testFunky_detectMismatchOfSourceType() {
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

    private long applyLamdaJ(Closure1<StringThing2> lammy) {
        StringThing2 stringThing2 = new StringThing2();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            lammy.apply(stringThing2);
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
