package org.funcito.benchmarks;

import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.funcito.internal.stub.StubFactory;
import org.funcito.internal.stub.utils.StubUtils;

import static org.funcito.Funcito.callsTo;
import static org.funcito.FuncitoGuava.functionFor;

/**
 * Copyright 2012 Project Funcito Contributors
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
public class FuncitoBenchmark extends SimpleBenchmark {
    private List<List<Object>> lists;

    private Function<List<Object>, Integer> sizeFuncitoFunction;
    private Function<List<Object>, Integer> sizeFunction = new Function<List<Object>, Integer>() {
        public Integer apply(List<Object> input) {
            return input.size();
        }
    };

    @Override
    protected void setUp() throws Exception {
        StubFactory.reset();
        sizeFuncitoFunction = null;
        Random rng = new Random();
        ImmutableList.Builder<List<Object>> builder = ImmutableList.builder();
        for (int i = 0; i < 0x10000; i++) {
            int n = rng.nextInt(0x100);
            builder.add(Collections.nCopies(n, new Object()));
        }
        lists = builder.build();
    }

    public int timeManual(int reps) {
        int tmp = 0;
        for (int i = 0; i < reps; i++) {
            Integer len = lists.get(i & 0xffff).size();
            tmp += len;
        }
        return tmp;
    }

    public int timeFunction(int reps) {
        int tmp = 0;
        for (int i = 0; i < reps; i++) {
            Integer len = sizeFunction.apply(lists.get(i & 0xffff));
            tmp += len;
        }
        return tmp;
    }

    public int timeFuncitoCGlib(int reps) {
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.CGLIB);
        int tmp = 0;
        if (sizeFuncitoFunction==null) {
            sizeFuncitoFunction = functionFor(callsTo(List.class).size());
        }
        for (int i = 0; i < reps; i++) {
            Integer len = sizeFuncitoFunction.apply(lists.get(i & 0xffff));
            tmp += len;
        }
        return tmp;
    }

    public int timeFuncitoJavassist(int reps) {
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.JAVASSIST);
        int tmp = 0;
        if (sizeFuncitoFunction==null) {
            sizeFuncitoFunction = functionFor(callsTo(List.class).size());
        }
        for (int i = 0; i < reps; i++) {
            Integer len = sizeFuncitoFunction.apply(lists.get(i & 0xffff));
            tmp += len;
        }
        return tmp;
    }

    public int timeFuncitoJavaProxy(int reps) {
        System.setProperty(StubUtils.FUNCITO_PROXY_PROVIDER_PROP, StubUtils.JAVAPROXY);
        int tmp = 0;
        if (sizeFuncitoFunction==null) {
            sizeFuncitoFunction = functionFor(callsTo(List.class).size());
        }
        for (int i = 0; i < reps; i++) {
            Integer len = sizeFuncitoFunction.apply(lists.get(i & 0xffff));
            tmp += len;
        }
        return tmp;
    }

    public static void main(String[] args) {
        Runner.main(FuncitoBenchmark.class, args);
    }
}