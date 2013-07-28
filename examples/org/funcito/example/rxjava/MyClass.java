/*
 * Copyright 2013 Project Funcito Contributors
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
package org.funcito.example.rxjava;

import rx.Observable;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

import java.util.Arrays;
import java.util.List;

import static org.funcito.FuncitoRxJava.*;
import static rx.Observable.*;

public class MyClass {

    private String myString;
    private Integer other;

    public static final MyClass stubbedCallsTo = callsTo(MyClass.class);

    public static final Func1<MyClass, String> getMyString = func1For(stubbedCallsTo.getMyString());
    public static final Func1<MyClass, Integer> getOther = func1For(stubbedCallsTo.getOther());
    
    // alternative single line syntax
    public static final Func1<Observable<MyClass>, String> getMyStringF2 = func1For(callsTo(MyClass.class).getMyString());

    // These demonstrate not only predicates, but also method argument binding
    public static final Func1<MyClass, Boolean> isLengthGT1 = func1For(stubbedCallsTo.isLengthGreaterThan(1));
    public static final Func1<MyClass, Boolean> isLengthGT3 = func1For(stubbedCallsTo.isLengthGreaterThan(3));

    // demonstrating Action1 creation around a method call that has a return value.
    public static final Action1<MyClass> incAndReturn = action1For(stubbedCallsTo.incAndReturn());

    // demonstrating Action1 creation around a void method call.

    // Note that for void version, the wrapped method call must be "prepared" before the Action1 is created.  Also,
    // to be able to assign to a static field via an initializer, prepareVoid() requires a static block.  If assignment
    // were to a non-static field, a non-static block would be sufficient.  If assignment to a Action1 variable is not
    // done in a field initializer (i.e., a local variable assignment or a field assignment in a method or execution
    // block), then no-extra "blocking" is needed for the prepareVoid() call.
    static {prepareVoid(stubbedCallsTo).inc();}
    public static final Action1<MyClass> inc = voidAction1();

    // demonstrating Action1 creation with extra type-safety
    static {prepareVoid(stubbedCallsTo).inc();}
    public static final Action1<MyClass> inc2 = voidAction1(MyClass.class);

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

        List<MyClass> list = Arrays.asList(m1, m2, m3);
        demoListMaps(list);
        demoListFilters(list);
        demoAction1Subscribes(list);

        m1.setMyString("This is my new value");
        m1.setOther(777);
        demoListMaps(list);
        demoListFilters(list);
        demoAction1Subscribes(list);
    }

    protected static void demoListMaps(List<MyClass> list) {
        Observable<String> strList1 = from(list).map(getMyString);
        Observable<String> strList2 = from(list).map(getMyStringF2);
        Observable<Integer> intList = from(list).map(getOther);

        strList1.toList().subscribe(printValues("xform1"));
        strList2.toList().subscribe(printValues("xform2"));
        intList.toList().subscribe(printValues("otherXform"));
    }

    protected static void demoListFilters(List<MyClass> list) {
        Observable<MyClass> filtered1 = from(list).filter(isLengthGT1);
        Observable<MyClass> filtered2 = from(list).filter(isLengthGT3);

        filtered1.toList().subscribe(printValues("select length > 1"));
        filtered2.toList().subscribe(printValues("select length > 3"));
    }

    protected static void demoAction1Subscribes(List<MyClass> list) {
        from(list).subscribe(incAndReturn);
        from(list).map(getOther).toList().subscribe(printValues("List has incremented each"));

        from(list).subscribe(inc);
        from(list).map(getOther).toList().subscribe(printValues("List has incremented each again"));

        from(list).subscribe(inc2);
        from(list).map(getOther).toList().subscribe(printValues("List has incremented a 3rd time"));
    }

    protected static <T> Action1<T> printValues(final String desc) {
        return new Action1<T>() {
            @Override
            public void call(T t) {
                System.out.println(desc + ": " + t);
            }
        };
    }
}
