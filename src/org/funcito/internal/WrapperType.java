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
package org.funcito.internal;

public enum WrapperType {
    GUAVA_FUNCTION, GUAVA_PREDICATE, FJ_F;
    
    public String toString() {
        String result = "";
        
        if (this == GUAVA_FUNCTION) {
            result = "Guava Function";
        } else if (this == GUAVA_PREDICATE) {
            result = "Guava Predicate";            
        } else if (this == FJ_F) {
            result = "Functional Java F (function)";
        }
        
        return result;
    }
}
