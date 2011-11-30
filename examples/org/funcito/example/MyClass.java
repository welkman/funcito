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
package org.funcito.example;

import static org.funcito.Funcito.callsTo;
import static org.funcito.Funcito.functionFor;

import com.google.common.base.Function;

public class MyClass {

    private String myString;
    private Integer other;

    public static final MyClass stubbedCallsTo = callsTo(MyClass.class);

    public static final Function<MyClass, String> getMyString = functionFor(stubbedCallsTo.getMyString());
    public static final Function<MyClass, Integer> getOther =   functionFor(stubbedCallsTo.getOther());

    // alternative single line syntax
    public static final Function<MyClass, String> getMyStringF2 = functionFor(callsTo(MyClass.class).getMyString());

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

    public static void main(String[] args) {
        MyClass me = new MyClass();
        me.setMyString("This is my value");
        me.setOther(321);
        printValues(me);

        me.setMyString("This is my new value");
        me.setOther(777);
        printValues(me);
    }

    protected static void printValues(MyClass me) {
        String result = MyClass.getMyString.apply(me);
        System.out.println(result);

        result = MyClass.getMyStringF2.apply(me);
        System.out.println(result);

        Integer intResult = MyClass.getOther.apply(me);
        System.out.println(intResult);
    }
}
