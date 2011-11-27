package org.funcito.internal;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * Copyright 2011 Project Funcito Contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class WrapperType_UT {
    @Test
    public void testGuavaFunction() {
        // test
        assertEquals("Guava Function", WrapperType.GUAVA_FUNCTION.toString());
    }
    
    @Test
    public void testGuavaPredicate() {
        // test
        assertEquals("Guava Predicate", WrapperType.GUAVA_PREDICATE.toString());
    }

    @Test
    public void testFJ_F() {
        // test
        assertEquals("Functional Java F (function)", WrapperType.FJ_F.toString());
    }
    
}
