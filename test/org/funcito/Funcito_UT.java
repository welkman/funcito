package org.funcito;

import com.google.common.base.Function;
import fj.F;
import jedi.functional.Functor;
import org.apache.commons.collections15.Transformer;
import org.junit.Test;
import rx.util.functions.Func1;

import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

public class Funcito_UT {

    @Test
    public void testCallsTo_cachesStubs() {
        Object proxy1 = callsTo(Object.class);
        Object proxy2 = callsTo(Object.class);
        assertSame(proxy1, proxy2);

        Object guavaStub = guava().callsTo(Object.class);
        assertSame(proxy1, guavaStub);

        Object jediStub = jedi().callsTo(Object.class);
        assertSame(proxy1, jediStub);

        Object fjStub = fj().callsTo(Object.class);
        assertSame(proxy1, fjStub);

        Object play2Stub = play2().callsTo(Object.class);
        assertSame(proxy1, play2Stub);

        Object rxJavaStub = rxJava().callsTo(Object.class);
        assertSame(proxy1, rxJavaStub);
    }

    @Test
    public void testCallsTo_usableByMultipleDelegates() throws Throwable {
        class MyClass {
            String method1() { return "method1"; }
            String method2() { return "method2"; }
            String method3() { return "method3"; }
            String method4() { return "method4"; }
            String method5() { return "method5"; }
            String method6() { return "method6"; }
        }

        MyClass callsTo = callsTo(MyClass.class);
        Function<MyClass,String> function = guava().functionFor(callsTo.method1());
        Functor<MyClass,String> functor = jedi().functorFor(callsTo.method2());
        F<MyClass,String> f = fj().fFor(callsTo.method3());
        play.libs.F.Function<MyClass,String> p2function = play2().functionFor(callsTo.method4());
        Transformer<MyClass,String> xform = collectGen().transformerFor(callsTo.method5());
        Func1<MyClass,String> func1 = rxJava().func1For(callsTo.method6());

        assertEquals("method1", function.apply(new MyClass()));
        assertEquals("method2", functor.execute(new MyClass()));
        assertEquals("method3", f.f(new MyClass()));
        assertEquals("method4", p2function.apply(new MyClass()));
        assertEquals("method5", xform.transform(new MyClass()));
        assertEquals("method6", func1.call(new MyClass()));
    }

}
