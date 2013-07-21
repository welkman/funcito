/*
 * Copyright 2011-2013 Project Funcito Contributors
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
package org.funcito.example.FJ;

import fj.data.List;

public class MySubClass extends MyClass {

    public MySubClass(String myString, Integer other) {
        super(myString, other);
    }

    @Override
    public String getMyString() {
        return super.getMyString() + "++";
    }

    @Override
    public Integer getOther() {
        return super.getOther() + 1000;
    }

    @Override
    public void inc() {
        setOther(super.getOther() + 2);
    }

    public static void main(String[] args) {
        MyClass m1 = new MySubClass("A", 1);
        MyClass m2 = new MySubClass("B", 2);
        MyClass m3 = new MySubClass("C", 3);

        List<MyClass> list = List.list(m1, m2, m3);
        demoListTransforms(list);
        demoListFilters(list);
        demoEffectsApplied(list);
    }
}
