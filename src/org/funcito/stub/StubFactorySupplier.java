package org.funcito.stub;

import java.io.Serializable;

import org.funcito.FuncitoException;
import org.funcito.stub.cglib.CglibStubFactory;
import org.funcito.stub.javassist.JavassistStubFactory;

import com.google.common.base.Supplier;

class StubFactorySupplier implements Supplier<StubFactory> {

	public StubFactory get() {
		StubFactory result = null;
		
        try {
            Class.forName("net.sf.cglib.proxy.Enhancer");
            result = new CglibStubFactory();
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("javassist.util.proxy.ProxyFactory");
                result = new JavassistStubFactory();
            } catch (ClassNotFoundException e2) {
                throw new FuncitoException("Error: Funcito requires the use of either the CGLib or Javassist code generation libraries." +
                        "Please ensure that you have one of the two libraries in your classpath.");
            }
        }
        
        return result;		
	}
}
