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

import org.funcito.Funcito;
import org.funcito.FuncitoException;
import org.funcito.internal.stub.*;
import org.funcito.internal.stub.cglib.CglibProxyFactory;
import org.funcito.internal.stub.javaproxy.JavaProxyProxyFactory;
import org.funcito.internal.stub.javassist.JavassistProxyFactory;
import org.junit.*;
import org.junit.rules.ExpectedException;

import org.mockito.*;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class StubUtils_UT {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    ClassFinder classFinder;

    @InjectMocks private ProxyUtils proxyUtils = new ProxyUtils();
    @Mock Handler logHandler;
    @Captor ArgumentCaptor<LogRecord> logCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Logger logger = Logger.getLogger(ProxyUtils.class.getName());
        logger.addHandler(logHandler);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Both() {
        when(classFinder.findOnClasspath(ProxyUtils.CGLIB_CLASS)).thenReturn(true);
        when(classFinder.findOnClasspath(ProxyUtils.JAVASSIST_CLASS)).thenReturn(true);
        
        // test
        ProxyFactory result = proxyUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof CglibProxyFactory);
        verify(logHandler).publish(logCaptor.capture());
        assertTrue(logCaptor.getValue().getMessage().contains("Found both CgLib and Javassist"));
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Cglib() {
        when(classFinder.findOnClasspath(ProxyUtils.CGLIB_CLASS)).thenReturn(true);
        when(classFinder.findOnClasspath(ProxyUtils.JAVASSIST_CLASS)).thenReturn(false);
        
        // test
        ProxyFactory result = proxyUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof CglibProxyFactory);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Javassist() {
        when(classFinder.findOnClasspath(ProxyUtils.CGLIB_CLASS)).thenReturn(false);
        when(classFinder.findOnClasspath(ProxyUtils.JAVASSIST_CLASS)).thenReturn(true);
        
        // test
        ProxyFactory result = proxyUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof JavassistProxyFactory);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Neither() {
        when(classFinder.findOnClasspath(ProxyUtils.CGLIB_CLASS)).thenReturn(false);
        when(classFinder.findOnClasspath(ProxyUtils.JAVASSIST_CLASS)).thenReturn(false);

        // test
        ProxyFactory result = proxyUtils.getExactlyOneFactoryFromClasspath();

        assertTrue(result instanceof JavaProxyProxyFactory);
        verify(logHandler).publish(logCaptor.capture());
        assertTrue(logCaptor.getValue().getMessage().contains("Found neither CgLib nor Javassist"));
    }

    @Test
    public void testGetOverrideBySystemProperty_None() {
        System.clearProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP);

        // test
        ProxyFactory result = proxyUtils.getOverrideBySystemProperty();
        
        assertNull(result);
    }
    
    @Test
    public void testGetOverrideBySystemProperty_Cglib() {
        when(classFinder.findOnClasspath(ProxyUtils.CGLIB_CLASS)).thenReturn(true);
        System.setProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP, Funcito.CGLIB);

        // test
        ProxyFactory result = proxyUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof CglibProxyFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_CglibSpecifiedButNotPresent() {
        when(classFinder.findOnClasspath(ProxyUtils.CGLIB_CLASS)).thenReturn(false);
        System.setProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP, Funcito.CGLIB);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("matching library is not found on the classpath");

        // test
        ProxyFactory result = proxyUtils.getOverrideBySystemProperty();
    }

    @Test
    public void testGetOverrideBySystemProperty_Javassist() {
        when(classFinder.findOnClasspath(ProxyUtils.JAVASSIST_CLASS)).thenReturn(true);
        System.setProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP, Funcito.JAVASSIST);

        // test
        ProxyFactory result = proxyUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof JavassistProxyFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_JavassistSpecifiedButNotPresent() {
        when(classFinder.findOnClasspath(ProxyUtils.JAVASSIST_CLASS)).thenReturn(false);
        System.setProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP, Funcito.JAVASSIST);

        thrown.expect(FuncitoException.class);
        thrown.expectMessage("matching library is not found on the classpath");

        // test
        ProxyFactory result = proxyUtils.getOverrideBySystemProperty();
    }

    @Test
    public void testGetOverrideBySystemProperty_JavaProxy() {
        System.setProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP, Funcito.JAVAPROXY);

        // test
        ProxyFactory result = proxyUtils.getOverrideBySystemProperty();

        assertTrue(result instanceof JavaProxyProxyFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_IllegalValue() {
        System.setProperty(Funcito.FUNCITO_PROXY_PROVIDER_PROP, "bogus");

        thrown.expect(FuncitoException.class);
        thrown.expectMessage(ProxyUtils.OVERRIDE_EXCEPTION);
        
        // test
        proxyUtils.getOverrideBySystemProperty();
    }
    
}
