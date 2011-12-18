package mockStubs;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class MethodQueingReturnValues extends ReturnsEmptyValues {

    public Object answer(InvocationOnMock invocationOnMock) {
        MethodWrapper.pushMethod(invocationOnMock.getMethod());
        return super.answer(invocationOnMock);
    }
}
