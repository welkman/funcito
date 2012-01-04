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
import org.funcito.internal.stub.javassist.JavassistStubFactory;

public class StubUtils {    
    public static final String FUNCITO_CODEGEN_LIB = "funcito.codegen.lib";
    public static final String CGLIB = "CGLIB";
    public static final String JAVASSIST = "JAVASSIST";
    static final String CGLIB_CLASS = "net.sf.cglib.proxy.Enhancer";
    static final String JAVASSIST_CLASS = "javassist.util.proxy.ProxyFactory";
    
    static final String OVERRIDE_EXCEPTION = "unknown value for system property: " + FUNCITO_CODEGEN_LIB;
    static final String NONE_ON_CLASSPATH_EXCEPTION = "Error: Funcito requires the use of either the CGLib or Javassist code generation libraries." +
                            "Please ensure that you have one of the two libraries in your classpath.";
    
    private ClassFinder classFinder = new ClassFinder();
    private PropertyFinder propertyFinder = new PropertyFinder();
    
    public StubFactory getExactlyOneFactoryFromClasspath() {
        StubFactory result = null;
        
        boolean foundCglib = classFinder.findOnClasspath(CGLIB_CLASS);
        boolean foundJavassist = classFinder.findOnClasspath(JAVASSIST_CLASS);
        
        if ((! foundCglib) && (! foundJavassist)) {
            throw new FuncitoException(NONE_ON_CLASSPATH_EXCEPTION);
        } else if (foundCglib && foundJavassist) {
            // no-op, return null
        } else {
            if (foundCglib) {
                result = new CglibStubFactory();
            } else {
                result = new JavassistStubFactory();
            }
        }
        
        return result;
    }
    
    public StubFactory getOverrideBySystemProperty() {
        StubFactory result = null;
    
        String prop = propertyFinder.findProperty(FUNCITO_CODEGEN_LIB);
        
        if (prop != null) {
            if (prop.toUpperCase().equals(CGLIB)) {
                result = new CglibStubFactory();
            } else if (prop.toUpperCase().equals(JAVASSIST)) {
                result = new JavassistStubFactory();
            } else {
                throw new FuncitoException(OVERRIDE_EXCEPTION); 
            }
        }
        
        return result;
    }
    
    // for testing
    void setClassFinder(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }
    
    void setPropertyFinder(PropertyFinder propertyFinder) {
        this.propertyFinder = propertyFinder;
    }
    
}
