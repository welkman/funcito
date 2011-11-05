package factory2;

import com.google.common.base.Function;

public class MyClass {

    private String myString;
    private String other;

    @FunctionWrapper("myString")
    public static final Function<MyClass, String> getMyString = FunctionFactory2.wrap(MyClass.class, "myString");

    @FunctionWrapper("myString")
    public static final Function<MyClass, String> getMyStringF2 = FunctionFactory2.wrap(MyClass.class, "myString");

    @FunctionWrapper("other")
    public static final Function<MyClass, String> getOther = FunctionFactory2.wrap(MyClass.class, "other");

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
