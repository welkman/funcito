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
package org.funcito.example.play;

import play.libs.F;
import play.libs.F.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.funcito.FuncitoPlay2.*;

public class MyClass {

    private String myString;
    private Integer other;

    public static final MyClass stubbedCallsTo = callsTo(MyClass.class);

    public static final Function<MyClass, String> getMyString = functionFor(stubbedCallsTo.getMyString());
    // alternative single line syntax
    public static final Function<MyClass, String> getMyStringF2 = functionFor(callsTo(MyClass.class).getMyString());

    // demonstrating F.Callback creation around a method call that has a return value.
    public static final F.Callback<MyClass> incAndReturn = callbackFor(stubbedCallsTo.incAndReturn());

    // demonstrating F.Callback creation around a void method call.

    // Note that for void version, the wrapped method call must be "prepared" before the F.Callback is created.  Also,
    // to be able to assign to a static field via an initializer, prepareVoid() requires a static block.  If assignment
    // were to a non-static field, a non-static block would be sufficient.  If assignment to a F.Callback variable is not
    // done in a field initializer (i.e., a local variable assignment or a field assignment in a method or execution
    // block), then no-extra "blocking" is needed for the prepareVoid() call.
    static {prepareVoid(stubbedCallsTo).inc();}
    public static final F.Callback<MyClass> inc = voidCallback();

    // demonstrating F.Callback creation with extra type-safety
    static {prepareVoid(stubbedCallsTo).inc();}
    public static final F.Callback<MyClass> inc2 = voidCallback(MyClass.class);


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

    @Override
    public String toString() {
        return getMyString();
    }

    public static void main(String[] args) throws Throwable {
        MyClass m1 = new MyClass("A", 1);
        MyClass m2 = new MyClass("B", 2);
        MyClass m3 = null;

        List<MyClass> list = Arrays.asList(m1,m2,m3);
        demoOptions(list);

        System.out.println("\nAfter Changing Value 1:");
        m1.setMyString("This is my new value");
        demoOptions(list);

        // I did not include demonstration code for using the Callback, because to do so requires creating a Promise,
        // which in the earliest version of Play! 2 requires the Scala system to be available at runtime.  So simply
        // see above field declarations for example of how to create Callback objects
    }

    protected static void demoOptions(List<MyClass> list) throws Throwable {
        List<F.Option<String>> optList1 = transform(list, getMyString);
        List<F.Option<String>> optList2 = transform(list, getMyStringF2);

        printValues("func1", optList1);
        System.out.println();
        printValues("func2", optList2);
    }

    protected static void printValues(String desc, List<F.Option<String>> list) {
        for(F.Option<String> opt: list) {
            System.out.println(desc + ": " + opt.getOrElse("Option Was Null"));
        }
    }

    /**
     * Play! Framework is not really a functional API, so it does not already
     */
    protected static List<F.Option<String>> transform(List<MyClass> list, Function<MyClass, String> func) throws Throwable {
        List<F.Option<String>> xformed = new ArrayList<F.Option<String>>();
        for (MyClass myClass: list) {
            F.Option<MyClass> opt = (myClass == null) ? new F.None<MyClass>() : new F.Some<MyClass>(myClass);
            xformed.add(opt.map(func));
        }
        return xformed;
    }
}
