import com.google.common.base.Predicate;
import info.piwai.funkyjfunctional.guava.Pred;
import org.funcito.Funcito;
import org.junit.BeforeClass;
import org.junit.Test;

import static info.piwai.funkyjfunctional.guava.FunkyGuava.withPred;
import static org.funcito.Funcito.stub;
import static org.funcito.Funcito.predFrom;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ComparePredicateWrappers {

    private static class BooleanThing {
        private Boolean myVal;
        public BooleanThing(Boolean myVal) { this.myVal = myVal; }
        public Boolean getVal() { return myVal; }
    }


    // Example 1 with FunkyJFunctional
    class F extends Pred<BooleanThing> {{ out = in.getVal(); }};
    Predicate<BooleanThing> funky = withPred(F.class);

    class Fx2 extends Pred<BooleanThing> {{ out = !in.getVal().booleanValue(); }};
    Predicate<BooleanThing> funky2 = withPred(Fx2.class);


    // Example 2 with my Mockito/mock-based type
    BooleanThing mockyStub = mockStubs.MethodWrapper.stubbed(BooleanThing.class);
    Predicate<BooleanThing> mocky = mockStubs.MethodWrapper.makePred(mockyStub.getVal());
    Predicate<BooleanThing> mockyX2 = mockStubs.MethodWrapper.makePred(!mockyStub.getVal());


    // Example 3 with normal manual definition of Predicate
    static Predicate<BooleanThing> manual = new Predicate<BooleanThing>() {
        public boolean apply(BooleanThing arg0) { return arg0.getVal(); }
    };

    static Predicate<BooleanThing> manualX2 = new Predicate<BooleanThing>() {
        public boolean apply(BooleanThing arg0) { return !arg0.getVal().booleanValue(); }
    };


    // Example 4 Funcito (CGLib)
    // TODO: alternative which would allow for type matching of the source object by using a source-typed factory for both stubbing and wrapping
//    FuncitoFactory<BooleanThing> factory = FuncitoFactory.for(BooleanThing.class);
//    BooleanThing stringThingStub = factory.makeStub();
//    Predicate<BooleanThing> cglibby = factory.predFrom(stringThingStub.getVal());

    BooleanThing cglibBooleanThingStub = stub(BooleanThing.class);
    Predicate<BooleanThing> cglibby = predFrom(cglibBooleanThingStub.getVal());
//    Predicate<BooleanThing> cglibby2 = predFrom(cglibBooleanThingStub.getVal() * 2);

    private static long timeManny = 0L;
    @BeforeClass
    public static void setupOnce() {
        long timeDirect = invoke();
        System.out.println("direct" + " = " + timeDirect);
        timeManny = apply(manual);
        System.out.println("manual" + " = " + timeManny);
    }

    @Test
    public void testLibbySpeed() {
        long libbyTime = apply(cglibby);
        System.out.println("CGLib" + " = " + libbyTime);
        assertTrue(10L * timeManny > libbyTime);
    }

    @Test
    public void testMockySpeed() {
        long mockyTime = apply(mocky);
        System.out.println("MyMockito-based" + " = " + mockyTime);
        assertTrue(10L * timeManny > mockyTime);
    }

    @Test
    public void testFunkySpeed() {
        long funkyTime = apply(funky);
        System.out.println("FunkyJFunctional" + " = " + funkyTime);
        assertTrue(10L * timeManny > funkyTime);
    }

    @Test
    public void testLibby_validateImproperStubCallsOutsideOfWrap() {
        cglibBooleanThingStub.getVal(); // should detect calling stub outside of a wrap call
        try {
            Predicate<BooleanThing> attempt = Funcito.predFrom(cglibBooleanThingStub.getVal());
            fail("Should not have succeeded");
        } catch (Exception e) {
            // happy path
            assertTrue(e.getMessage().contains("Multiple method calls"));
        }
    }

    class OtherThing { // does NOT extend BooleanThing or implement common interface, but they look identical
        private Boolean value;
        public OtherThing(Boolean value) { this.value = value; }
        public Boolean getVal() { return value; }
    }

    @Test
    public void testLibby_mismatchOfSourceType() {
        OtherThing otherThingStub = Funcito.stub(OtherThing.class);
        try {
            Predicate<BooleanThing> func = Funcito.predFrom(otherThingStub.getVal());
            fail("Should not have allowed this");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testMocky_mismatchOfSourceType() {
        OtherThing otherThingStub = mockStubs.MethodWrapper.stubbed(OtherThing.class);
        try {
            Predicate<BooleanThing> func = mockStubs.MethodWrapper.makePred(otherThingStub.getVal());
            fail("Should not have allowed this");
        } catch (Exception e) {
            // Happy Path
        }
    }

    @Test
    public void testFunky_mismatchOfSourceType() {
        class F3 extends Pred<OtherThing> {{ out = in.getVal(); }};
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
