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
package org.funcito.example.Jedi;

import jedi.functional.Filter;
import jedi.functional.Functor;

import java.util.Arrays;
import java.util.List;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.select;
import static org.funcito.FuncitoJedi.*;

public class MyClass {

    private String myString;
    private Integer other;

    public static final MyClass stubbedCallsTo = callsTo(MyClass.class);

    public static final Functor<MyClass, String> getMyString = functorFor(stubbedCallsTo.getMyString());
    public static final Functor<MyClass, Integer> getOther = functorFor(stubbedCallsTo.getOther());

    // alternative single line syntax
    public static final Functor<MyClass, String> getMyStringF2 = functorFor(callsTo(MyClass.class).getMyString());

    // These demonstrate not only predicates, but also method argument binding
    public static final Filter<MyClass> isLengthGT1 = filterFor(stubbedCallsTo.isLengthGreaterThan(1));
    public static final Filter<MyClass> isLengthGT3 = filterFor(stubbedCallsTo.isLengthGreaterThan(3));

    public MyClass(String myString, Integer other) {
        this.myString = myString;
        this.other = other;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String value) {
        this.myString = value;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public boolean isLengthGreaterThan(int lower) {
        return getMyString().length() > lower;
    }

    @Override
    public String toString() {
        return getMyString();
    }

    public static void main(String[] args) {
        MyClass m1 = new MyClass("A", 1);
        MyClass m2 = new MyClass("B", 2);
        MyClass m3 = new MyClass("C", 3);

        List<MyClass> list = Arrays.asList(m1,m2,m3);
        demoListTransforms(list);
        demoListFilters(list);

        m1.setMyString("This is my new value");
        m1.setOther(777);
        demoListTransforms(list);
        demoListFilters(list);
    }

    protected static void demoListTransforms(List<? extends MyClass> list) {
        List<String> strList1 = collect(list, getMyString);
        List<String> strList2 = collect(list, getMyStringF2);
        List<Integer> intList = collect(list, getOther);

        printValues("func1", strList1);
        printValues("func2", strList2);
        printValues("otherFunc", intList);
    }

    protected static void demoListFilters(List<MyClass> list) {
        List<MyClass> filtered1 = select(list, isLengthGT1);
        List<MyClass> filtered2 = select(list, isLengthGT3);

        printValues("filter length > 1", filtered1);
        printValues("filter length > 3", filtered2);
    }

    protected static void printValues(String desc, List<?> list) {
        System.out.println(desc + ": " + list);
    }
}
