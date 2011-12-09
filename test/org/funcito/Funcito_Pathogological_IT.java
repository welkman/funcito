package org.funcito;

import static org.funcito.Funcito.callsTo;
import static org.funcito.Funcito.functionFor;
import static org.junit.Assert.assertEquals;

import org.funcito.internal.Invokable;
import org.junit.Test;

import com.google.common.base.Function;

class Worker {
    private int badgeNum;
    private int ssn;

    public Worker(int badgeNum, int ssn) {
        this.badgeNum = badgeNum;
        this.ssn = ssn;
    }

    public int getBadgeNum() { return badgeNum; }
    public int getSsn() { return ssn; }
}

class WorkerThread {
    private static final Worker CALLS_TO = callsTo(Worker.class); 
 
    public void workWithBadgeNum() {
        //Invokable invokable = CALLS_TO.getBadgeNum();
    }
    
    public void workWithSsn() {
        
    }
    public static final Function<Worker,Integer> GET_BADGE_NUM  = functionFor(CALLS_TO.getBadgeNum());
 }

public class Funcito_Pathogological_IT {

    //@Test
    public void testFunctionFor_AllowUpcastToExtensionGenericType() {
        class Generic<T> {
            public Integer getVal() {
                return 123;
            }
        }
        Function<Generic<? extends Object>, Integer> stringFunc = functionFor(callsTo(Generic.class).getVal());
        Generic<Integer> integerGeneric = new Generic<Integer>();

        assertEquals(123, stringFunc.apply(integerGeneric).intValue());
    }
}

