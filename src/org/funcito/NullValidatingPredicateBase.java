package org.funcito;

import org.funcito.internal.InvokableState;

import java.lang.reflect.Method;

public class NullValidatingPredicateBase<T> extends FunctionalBase<T,Boolean> {
    String apiPredicateClass;
    String altMethod;

    public NullValidatingPredicateBase(InvokableState state, Class<?> apiPredicateClass, Method altMethod) {
        super(state);
        this.apiPredicateClass = apiPredicateClass.getName();
        this.altMethod = altMethod.toGenericString();
    }

    @Override
    protected void validateReturnValue(Object retVal) {
        if (retVal==null) {
            throw new FuncitoException(apiPredicateClass + " had a null Boolean return value.\n " +
                apiPredicateClass + " expects a non-null Boolean so that it can be autoboxed to a primitive boolean.\n " +
                "You might consider the alternate method: " + altMethod);
        }
    }
}
