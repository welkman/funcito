import com.google.common.base.Predicate;
import info.piwai.funkyjfunctional.guava.Pred;
import org.funcito.FuncitoGuava;
import org.junit.BeforeClass;
import org.junit.Test;

import static info.piwai.funkyjfunctional.guava.FunkyGuava.withPred;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ComparePredicateWrappers {

    private static class BooleanThing {
        private Boolean myVal;
        public BooleanThing(Boolean myVal) { this.myVal = myVal; }
        public Boolean getVal() { return myVal; }
    }


    // Example 1 with FunkyJFunctional
    class F extends Pred<BooleanThing> {{ out = in.getVal(); }}
    Predicate<BooleanThing> funky = withPred(F.class);

    class Fx2 extends Pred<BooleanThing> {{ out = !in.getVal(); }}
    Predicate<BooleanThing> funkyNot = withPred(Fx2.class);


    // Example 2 with my Mockito/mock-based type
    BooleanThing mockitoStub = mockStubs.MethodWrapper.stubbed(BooleanThing.class);
    Predicate<BooleanThing> mockitoPred = mockStubs.MethodWrapper.makePred(mockitoStub.getVal());
    Predicate<BooleanThing> mockitoPredNot = mockStubs.MethodWrapper.makePred(!mockitoStub.getVal());


    // Example 3 with normal manual definition of Predicate
    static Predicate<BooleanThing> manual = new Predicate<BooleanThing>() {
        public boolean apply(BooleanThing arg0) { return arg0.getVal(); }
    };
    static Predicate<BooleanThing> manualX2 = new Predicate<BooleanThing>() {
        public boolean apply(BooleanThing arg0) { return !arg0.getVal(); }
    };


    // Example 4 Funcito (CGLib)
    BooleanThing funcitoBooleanThingStub = FuncitoGuava.callsTo(BooleanThing.class);
    Predicate<BooleanThing> funcitoPred = FuncitoGuava.predicateFor(funcitoBooleanThingStub.getVal());

    private static long timeManual = 0L;
    @BeforeClass
    public static void setupOnce() {
        long timeDirect = invoke();
        System.out.println("direct" + " = " + timeDirect);
        timeManual = apply(manual);
        System.out.println("manual" + " = " + timeManual);
    }

    @Test
    public void testFuncitoSpeed() {
        long funcitoTime = apply(funcitoPred);
        System.out.println("Funcito (CGLib)" + " = " + funcitoTime);
        assertTrue(10L * timeManual > funcitoTime);
    }

    @Test
    public void testMockitoSpeed() {
        long mockitoTime = apply(mockitoPred);
        System.out.println("MyMockito-based" + " = " + mockitoTime);
        assertTrue(10L * timeManual > mockitoTime);
    }

    @Test
    public void testFunkySpeed() {
        long funkyTime = apply(funky);
        System.out.println("FunkyJFunctional" + " = " + funkyTime);
        assertTrue(10L * timeManual > funkyTime);
    }

    class OtherThing { // does NOT extend BooleanThing or implement common.common interface, but they look identical
        private Boolean value;
        public OtherThing(Boolean value) { this.value = value; }
        public Boolean getVal() { return value; }
    }

    @Test
    public void testMockito_mismatchOfSourceType() {
        OtherThing otherThingStub = mockStubs.MethodWrapper.stubbed(OtherThing.class);
        try {
            Predicate<BooleanThing> pred = mockStubs.MethodWrapper.makePred(otherThingStub.getVal());
            fail("Should not have allowed this");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testFunky_mismatchOfSourceType() {
        class F3 extends Pred<OtherThing> {{ out = in.getVal(); }}
        try {
            // This has static (compile-time) safety, so test passes by virtue of static source type safety
//            Predicate<BooleanThing> funky2 = withPred(F3.class);
//            fail("Should not have allowed this");
        } catch (Exception e) {
            // Happy Path
        }
    }

    //----------------- HELPER METHODS ------------------------------

    private static long apply(Predicate<BooleanThing> function) {
        BooleanThing booleanThing = new BooleanThing(true);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
              function.apply(booleanThing);
        }
        long duration = System.currentTimeMillis() - startTime;
        return duration;
    }
    private static long invoke() {
        BooleanThing booleanThing = new BooleanThing(true);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
              booleanThing.getVal();
        }
        long duration = System.currentTimeMillis() - startTime;
        return duration;
    }
}
