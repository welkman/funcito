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

import static org.junit.Assert.*;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.*;
import org.funcito.internal.stub.cglib.CglibStubFactory;
import org.funcito.internal.stub.javaproxy.JavaProxyStubFactory;
import org.funcito.internal.stub.javassist.JavassistStubFactory;
import org.junit.*;
import org.junit.rules.ExpectedException;

import org.mockito.*;

import static org.mockito.Mockito.*;

public class StubUtils_UT {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    ClassFinder classFinder;

    @InjectMocks private StubUtils stubUtils = new StubUtils();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Both() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(true);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(true);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof CglibStubFactory);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Cglib() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(true);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(false);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof CglibStubFactory);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Javassist() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(false);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(true);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof JavassistStubFactory);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Neither() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(false);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(false);

        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();

        assertTrue(result instanceof JavaProxyStubFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_None() {
        assertNull(System.getProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP));

        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertNull(result);
    }
    
    @Test
    public void testGetOverrideBySystemProperty_Cglib() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(true);
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.CGLIB);

        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof CglibStubFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_CglibSpecifiedButNotPresent() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(false);
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.CGLIB);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("matching library is not found on the classpath");

        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
    }

    @Test
    public void testGetOverrideBySystemProperty_Javassist() {
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(true);
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.JAVASSIST);

        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof JavassistStubFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_JavassistSpecifiedButNotPresent() {
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(false);
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.JAVASSIST);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("matching library is not found on the classpath");

        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
    }

    @Test
    public void testGetOverrideBySystemProperty_JavaProxy() {
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.JAVAPROXY);

        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();

        assertTrue(result instanceof JavaProxyStubFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_IllegalValue() {
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, "bogus");

        thrown.expect(FuncitoException.class);
        thrown.expectMessage(StubUtils.OVERRIDE_EXCEPTION);
        
        // test
        stubUtils.getOverrideBySystemProperty();
    }
    
}
