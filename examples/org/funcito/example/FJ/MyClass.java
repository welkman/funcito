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

import fj.Effect;
import fj.F;
import fj.data.List;

import static org.funcito.FuncitoFJ.*;

public class MyClass {

    private String myString;
    private Integer other;

    public static final MyClass stubbedCallsTo = callsTo(MyClass.class);

    public static final F<MyClass, String> getMyString = fFor(stubbedCallsTo.getMyString());
    public static final F<MyClass, Integer> getOther = fFor(stubbedCallsTo.getOther());

    // alternative single line syntax
    public static final F<MyClass, String> getMyStringF2 = fFor(callsTo(MyClass.class).getMyString());

    // These demonstrate not only predicates, but also method argument binding
    public static final F<MyClass,Boolean> isLengthGT1 = fFor(stubbedCallsTo.isLengthGreaterThan(1));
    public static final F<MyClass,Boolean> isLengthGT3 = fFor(stubbedCallsTo.isLengthGreaterThan(3));

    // demonstrating Effect creation around a method call that has a return value.
    public static final Effect<MyClass> incAndReturn = effectFor(stubbedCallsTo.incAndReturn());

    // demonstrating Effect creation around a void method call.

    // Note that for void version, the wrapped method call must be "prepared" before the Effect is created.  Also,
    // to be able to assign to a static field via an initializer, prepareVoid() requires a static block.  If assignment
    // were to a non-static field, a non-static block would be sufficient.  If assignment to a Effect variable is not
    // done in a field initializer (i.e., a local variable assignment or a field assignment in a method or execution
    // block), then no-extra "blocking" is needed for the prepareVoid() call.
    static {prepareVoid(stubbedCallsTo).inc();}
    public static final Effect<MyClass> inc = voidEffect();

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

    public void inc() {
        ++other;
    }

    public Integer incAndReturn() {
        inc();
        return other;
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

        List<MyClass> list = List.list(m1, m2, m3);
        demoListTransforms(list);
        demoListFilters(list);
        demoEffectsApplied(list);

        m1.setMyString("This is my new value");
        m1.setOther(777);
        demoListTransforms(list);
        demoListFilters(list);
        demoEffectsApplied(list);
    }

    protected static void demoListTransforms(List<MyClass> list) {
        List<String> strList1 = list.map(getMyString);
        List<String> strList2 = list.map(getMyStringF2);
        List<Integer> intList = list.map(getOther);

        printValues("func1", strList1);
        printValues("func2", strList2);
        printValues("otherFunc", intList);
    }

    protected static void demoListFilters(List<MyClass> list) {
        List<MyClass> filtered1 = list.filter(isLengthGT1);
        List<MyClass> filtered2 = list.filter(isLengthGT3);

        printValues("filter length > 1", filtered1);
        printValues("filter length > 3", filtered2);
    }

    protected static void demoEffectsApplied(List<MyClass> list) {
        list.foreach(incAndReturn);
        printValues("List has incremented each", list.map(getOther));

        list.foreach(inc);
        printValues("List has incremented each again", list.map(getOther));
    }

    protected static void printValues(String desc, List<?> list) {
        System.out.println(desc + ": " + list.toCollection());
    }
}
