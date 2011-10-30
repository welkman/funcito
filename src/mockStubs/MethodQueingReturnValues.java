package mockStubs;

import org.mockito.internal.returnvalues.EmptyReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class MethodQueingReturnValues extends EmptyReturnValues {
    public Object valueFor(InvocationOnMock invocation) {
        MethodWrapper.pushMethod(invocation.getMethod());
        return super.valueFor(invocation);
    }
}
