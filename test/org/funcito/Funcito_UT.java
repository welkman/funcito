package org.funcito;

import com.google.common.base.Function;
import fj.F;
import jedi.functional.Functor;
import org.apache.commons.collections15.Transformer;
import org.funcito.internal.stub.ProxyFactory;
import org.funcito.internal.stub.cglib.CglibProxyFactory;
import org.funcito.internal.stub.javaproxy.JavaProxyProxyFactory;
import org.funcito.internal.stub.javassist.JavassistProxyFactory;
import org.junit.Before;
import org.junit.Test;
import rx.functions.Func1;

import static org.funcito.Funcito.*;
import static org.junit.Assert.*;

public class Funcito_UT {

    @Before
    public void setUp() {
        ProxyFactory.reset(); // not to be used in real programs!!
    }

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

    @Test
    public void testSetProxyProviderProperty() {
        Funcito.setProxyProviderProperty(Funcito.JAVAPROXY);
        assertEquals(JavaProxyProxyFactory.class, ProxyFactory.instance().getClass());

        ProxyFactory.reset(); // not to be used in real programs!!
        Funcito.setProxyProviderProperty(Funcito.JAVASSIST);
        assertEquals(JavassistProxyFactory.class, ProxyFactory.instance().getClass());

        ProxyFactory.reset(); // not to be used in real programs!!
        Funcito.setProxyProviderProperty(Funcito.CGLIB);
        assertEquals(CglibProxyFactory.class, ProxyFactory.instance().getClass());
    }


    @Test
    public void testSetProxyProviderProperty_cannotChangeAfterInstantiated() {
        Funcito.setProxyProviderProperty(Funcito.JAVAPROXY); // no instantiation yet, so...
        Funcito.setProxyProviderProperty(Funcito.JAVASSIST); // can call again
        assertEquals(JavassistProxyFactory.class, ProxyFactory.instance().getClass());

        // however, now it has been instantiated above, so changed property has no effect
        Funcito.setProxyProviderProperty(Funcito.CGLIB);
        assertFalse(CglibProxyFactory.class.equals(ProxyFactory.instance().getClass()));
        assertEquals(JavassistProxyFactory.class, ProxyFactory.instance().getClass());
    }

    @Test
    public void test() {
        Funcito.setProxyProvider(new JavaProxyProxyFactory());
        assertEquals(JavaProxyProxyFactory.class, ProxyFactory.instance().getClass());

        Funcito.setProxyProvider(new JavassistProxyFactory());
        assertEquals(JavassistProxyFactory.class, ProxyFactory.instance().getClass());

        Funcito.setProxyProvider(new CglibProxyFactory());
        assertEquals(CglibProxyFactory.class, ProxyFactory.instance().getClass());
    }
}
