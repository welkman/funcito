package org.funcito;

import com.google.common.base.Function;
import fj.F;
import jedi.functional.Functor;
import org.junit.Test;

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
    }

    @Test
    public void testCallsTo_usableByMultipleDelegates() {
        class MyClass {
            String method1() { return "method1"; }
            String method2() { return "method2"; }
            String method3() { return "method3"; }
        }

        MyClass callsTo = callsTo(MyClass.class);
        Function<MyClass,String> function = guava().functionFor(callsTo.method1());
        Functor<MyClass,String> functor = jedi().functorFor(callsTo.method2());
        F<MyClass,String> f = fj().fFor(callsTo.method3());

        assertEquals("method1", function.apply(new MyClass()));
        assertEquals("method2", functor.execute(new MyClass()));
        assertEquals("method3", f.f(new MyClass()));
    }

}
