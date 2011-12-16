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

    @Mock
    PropertyFinder propertyFinder;

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
        
        assertNull(result);
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

        thrown.expect(FuncitoException.class);
        thrown.expectMessage(StubUtils.NONE_ON_CLASSPATH_EXCEPTION);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();        
    }

    @Test
    public void testGetOverrideBySystemProperty_None() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn(null);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertNull(result);
    }
    
    @Test
    public void testGetOverrideBySystemProperty_Cglib() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn(StubUtils.CGLIB);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof CglibStubFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_Javassist() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn(StubUtils.JAVASSIST);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof JavassistStubFactory);
    }
    
    @Test
    public void testGetOverrideBySystemProperty_IllegalValue() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn("bogus");
        
        thrown.expect(FuncitoException.class);
        thrown.expectMessage(StubUtils.OVERRIDE_EXCEPTION);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
    }
    
}
