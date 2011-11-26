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

package org.funcito.stub;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public abstract class StubFactory {
    private static final Supplier<StubFactory> supplier = Suppliers.memoize(new StubFactorySupplier());
    
    public static StubFactory instance() {
        return supplier.get();
    }
    
    public abstract <T> T stub(Class<T> clazz);
}
