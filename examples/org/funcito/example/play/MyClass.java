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
package org.funcito.example.play;

import play.libs.F;
import play.libs.F.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.funcito.FuncitoPlay2.*;

public class MyClass {

    private String myString;

    public static final MyClass stubbedCallsTo = callsTo(MyClass.class);

    public static final Function<MyClass, String> getMyString = functionFor(stubbedCallsTo.getMyString());
    // alternative single line syntax
    public static final Function<MyClass, String> getMyStringF2 = functionFor(callsTo(MyClass.class).getMyString());

    public MyClass(String myString) {
        this.myString = myString;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String value) {
        this.myString = value;
    }

    @Override
    public String toString() {
        return getMyString();
    }

    public static void main(String[] args) throws Throwable {
        MyClass m1 = new MyClass("A");
        MyClass m2 = new MyClass("B");
        MyClass m3 = null;

        List<MyClass> list = Arrays.asList(m1,m2,m3);
        demoOptions(list);

        System.out.println("\nAfter Changing Value 1:");
        m1.setMyString("This is my new value");
        demoOptions(list);
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

    protected static List<F.Option<String>> transform(List<MyClass> list, Function<MyClass, String> func) throws Throwable {
        List<F.Option<String>> xformed = new ArrayList<F.Option<String>>();
        for (MyClass myClass: list) {
            F.Option<MyClass> opt = (myClass == null) ? new F.None<MyClass>() : new F.Some<MyClass>(myClass);
            xformed.add(opt.map(func));
        }
        return xformed;
    }
}
