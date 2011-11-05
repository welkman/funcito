package factory1;

import com.google.common.base.Function;

public class MyClass {

    static { FunctionFactory.wrap(MyClass.class);}

    private String myString;
    private String other;

    @FunctionWrapper("myString")
    public static Function<MyClass, String> getMyString;

    @FunctionWrapper("myString")
    public static Function<MyClass, String> getMyStringF2;

    @FunctionWrapper("other")
    public static Function<MyClass, String> getOther;

    @FunctionWrapped("myString")
    public String getMyString() {
        return myString;
    }

    public void setMyString(String value) {
        this.myString = value;
    }

    @FunctionWrapped("other")
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public static void main(String[] args) {
        MyClass me = new MyClass();
        me.setMyString("This is my value");
        me.setOther("other value");

        printValues(me);
    }

    protected static void printValues(MyClass me) {
        String result = MyClass.getMyString.apply(me);
        System.out.println(result);

        result = MyClass.getMyStringF2.apply(me);
        System.out.println(result);

        result = MyClass.getOther.apply(me);
        System.out.println(result);
    }
}
