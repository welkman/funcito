/**
 * Copyright 2011 Project Funcito Contributors
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.funcito.internal.stub.utils;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.StubFactory;
import org.funcito.internal.stub.cglib.CglibStubFactory;
import org.funcito.internal.stub.javaproxy.JavaProxyStubFactory;
import org.funcito.internal.stub.javassist.JavassistStubFactory;

import java.util.logging.Logger;

public class StubUtils {

    Logger logger = Logger.getLogger(StubUtils.class.getName());

    /** System property that forces selection of a specific proxy provider */
    public static final String FUNCITO_PROXY_PROVIDER_PROP = "funcito.proxy.provider";

    /** Value for property {@link #FUNCITO_PROXY_PROVIDER_PROP} to force CGLib as proxy provider */
    public static final String CGLIB = "CGLIB";
    /** Value for property {@link #FUNCITO_PROXY_PROVIDER_PROP} to force Javassist as proxy provider */
    public static final String JAVASSIST = "JAVASSIST";
    /** Value for property {@link #FUNCITO_PROXY_PROVIDER_PROP} to force J2SE dynamic proxies as proxy provider */
    public static final String JAVAPROXY = "JAVAPROXY";

    static final String CGLIB_CLASS = "net.sf.cglib.proxy.Enhancer";
    static final String JAVASSIST_CLASS = "javassist.util.proxy.ProxyFactory";
    
    static final String OVERRIDE_EXCEPTION = "unknown value for system property: " + FUNCITO_PROXY_PROVIDER_PROP;

    private ClassFinder classFinder = new ClassFinder();
    private PropertyFinder propertyFinder = new PropertyFinder();
    
    public StubFactory getExactlyOneFactoryFromClasspath() {
        boolean foundCglib = classFinder.findOnClasspath(CGLIB_CLASS);
        boolean foundJavassist = classFinder.findOnClasspath(JAVASSIST_CLASS);

        if (foundCglib) {
            if (foundJavassist) {
                // if both code-gen libs available on classpath, Funcito defaults to Cglib
                logger.warning("Found both CgLib and Javassist on classpath. Using CgLib: "
                        + "set System property '" + StubUtils.FUNCITO_PROXY_PROVIDER_PROP + "' to change.");
            }
            return new CglibStubFactory();
        } else if (foundJavassist) {
            return new JavassistStubFactory();
        }
        // if neither available on classpath, Funcito defaults to Java dynamic Proxy, which only proxies interfaces.
        logger.warning("Found neither CgLib nor Javassist on classpath. Using Java dynamic Proxies: "
                + "if you need to wrap methods on classes instead of only interfaces, please include either Cglib or Javasssist in classpath");
        return new JavaProxyStubFactory();
    }
    
    public StubFactory getOverrideBySystemProperty() {
        StubFactory result = null;
    
        String prop = propertyFinder.findProperty(FUNCITO_PROXY_PROVIDER_PROP);
        
        if (prop != null) {
            boolean foundCglib = classFinder.findOnClasspath(CGLIB_CLASS);
            boolean foundJavassist = classFinder.findOnClasspath(JAVASSIST_CLASS);
            if (prop.toUpperCase().equals(CGLIB)) {
                validateSystemProperty(foundCglib, CGLIB);
                result = new CglibStubFactory();
            } else if (prop.toUpperCase().equals(JAVASSIST)) {
                validateSystemProperty(foundJavassist, JAVASSIST);
                result = new JavassistStubFactory();
            } else if (prop.toUpperCase().equals(JAVAPROXY)) {
                result = new JavaProxyStubFactory();
            } else {
                throw new FuncitoException(OVERRIDE_EXCEPTION); 
            }
        }
        
        return result;
    }

    private void validateSystemProperty(boolean libraryFound, String libraryChoice) {
        if (!libraryFound) {
            throw new FuncitoException("System property " + FUNCITO_PROXY_PROVIDER_PROP + " has been set to " + libraryChoice + " but a matching library is not found on the classpath.");
        }
    }
}
