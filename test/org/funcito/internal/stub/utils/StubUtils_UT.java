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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.funcito.FuncitoException;
import org.funcito.internal.stub.*;
import org.funcito.internal.stub.cglib.CglibStubFactory;
import org.funcito.internal.stub.javassist.JavassistStubFactory;
import org.junit.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StubUtils_UT {
    private StubUtils stubUtils = new StubUtils();
    
    @Mock
    ClassFinder classFinder;

    @Mock
    PropertyFinder propertyFinder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Both() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(true);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(true);
        stubUtils.setClassFinder(classFinder);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result == null);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Cglib() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(true);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(false);
        stubUtils.setClassFinder(classFinder);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof CglibStubFactory);
    }

    @Test
    public void testGetExactlyOneFactoryFromClasspath_Javassist() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(false);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(true);
        stubUtils.setClassFinder(classFinder);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
        
        assertTrue(result instanceof JavassistStubFactory);
    }

    @Test(expected = FuncitoException.class)
    public void testGetExactlyOneFactoryFromClasspath_Neither() {
        when(classFinder.findOnClasspath(StubUtils.CGLIB_CLASS)).thenReturn(false);
        when(classFinder.findOnClasspath(StubUtils.JAVASSIST_CLASS)).thenReturn(false);
        stubUtils.setClassFinder(classFinder);
        
        // test
        StubFactory result = stubUtils.getExactlyOneFactoryFromClasspath();
    }

    @Test
    public void testGetOverrideBySystemProperty_None() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn(null);
        stubUtils.setPropertyFinder(propertyFinder);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertEquals(null, result);
    }
    
    @Test
    public void testGetOverrideBySystemProperty_Cglib() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn(StubUtils.CGLIB);
        stubUtils.setPropertyFinder(propertyFinder);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof CglibStubFactory);
    }

    @Test
    public void testGetOverrideBySystemProperty_Javassist() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn(StubUtils.JAVASSIST);
        stubUtils.setPropertyFinder(propertyFinder);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
        
        assertTrue(result instanceof JavassistStubFactory);
    }
    
    @Test(expected = FuncitoException.class)
    public void testGetOverrideBySystemProperty_IllegalValue() {
        when(propertyFinder.findProperty(StubUtils.FUNCITO_CODEGEN_LIB)).thenReturn("bogus");
        stubUtils.setPropertyFinder(propertyFinder);
        
        // test
        StubFactory result = stubUtils.getOverrideBySystemProperty();
    }
    
}
