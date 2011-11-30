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

import org.funcito.FuncitoException;

class InvokableState {
    private Invokable invokable = null;
    
    void put(Invokable invokable) {
        if (invokable == null) {
            throw new FuncitoException("Internal error: attempt to 'put' null invokable");
        }
        if (isFull()) {
            throw new FuncitoException("Internal error: consecutive attempts to 'put' invokable");            
        }
        
        this.invokable = invokable;
    }
   
    Invokable get() {        
        if (isEmpty()) {
            throw new FuncitoException("Internal error: consecutive attempts to 'get' invokable");            
        }
        
        Invokable result = invokable;        
        invokable = null;
        
        return result;
    }
    
    boolean isFull() {
        return invokable != null;
    }
    
    boolean isEmpty() {
        return invokable == null;
    }
    
    void clear() {
        invokable = null;
    }
}
