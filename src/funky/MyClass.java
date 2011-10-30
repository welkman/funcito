package funky;

import static  info.piwai.funkyjfunctional.guava.FunkyGuava.*;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import info.piwai.funkyjfunctional.guava.Func;
import info.piwai.funkyjfunctional.guava.Pred;

interface Mine {
    public String getMyString();
    public Integer getOther();
}

class GetMyString extends Func<Mine, String>  {{out = in.getMyString();}}
class GetOther    extends Func<Mine, Integer> {{out = in.getOther();}}
class Contains    extends Pred<Mine>          {public Contains(String s){out = in.getMyString().contains(s);}}

public class MyClass implements Mine{
    private String myString;
    private Integer other;

    public static Function<Mine, String> getMyString = withFunc(GetMyString.class);
    public static Function<Mine, String> getMyStringF2 = withFunc(GetMyString.class);
    public static Function<Mine, Integer> getOther = withFunc(GetOther.class);
    public static Predicate<Mine> contains = withPred(Contains.class, "new");

    public String getMyString() {
        return myString;
    }

    public void setMyString(String value) {
        this.myString = value;
    }

    final public Integer getOther() {
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

        System.out.println("Contains 'new' = " + MyClass.contains.apply(me));
    }
}
