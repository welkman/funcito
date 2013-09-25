package org.funcito.functorfactory;

import org.funcito.functorbase.BasicFunctor;
import org.funcito.functorbase.FunctorBase;
import org.funcito.internal.Invokable;
import org.funcito.internal.InvokableState;
import org.funcito.mode.Mode;
import org.funcito.mode.UntypedMode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FunctorFactory_UT {

    private FunctorFactory factory = FunctorFactory.instance();
    @Mock private InvokableState state;
    @Mock private UntypedMode untypedMode;
    @Mock private Mode mode;
    @Mock private FunctorBase functorBase;
    @Mock private Iterator<Invokable> iter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(state.iterator()).thenReturn(iter);
    }

    @Test
    public void testMakeFunctionalBase_UntypedMode() {
        when(untypedMode.makeBase(state)).thenReturn(functorBase);

        FunctorBase result = factory.makeFunctionalBase(state, untypedMode);

        assertSame(functorBase, result);
    }

    @Test
    public void testMakeFunctionalBase_UntypedModeNull() {
        when(untypedMode.makeBase(state)).thenReturn(functorBase);

        FunctorBase result = factory.makeFunctionalBase(state, (UntypedMode)null);

        assertEquals(BasicFunctor.class, result.getClass());
    }

    @Test
    public void testMakeFunctionalBase_TypedMode() {
        when(mode.makeBase(state)).thenReturn(functorBase);

        FunctorBase result = factory.makeFunctionalBase(state, mode);

        assertSame(functorBase, result);
    }

    @Test
    public void testMakeFunctionalBase_TypedModeNull() {
        when(mode.makeBase(state)).thenReturn(functorBase);

        FunctorBase result = factory.makeFunctionalBase(state, (Mode)null);

        assertEquals(BasicFunctor.class, result.getClass());
    }
}
