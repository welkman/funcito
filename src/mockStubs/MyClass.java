package mockStubs;

import com.google.common.base.Function;

public class MyClass {

    private String myString;
    private Integer other;

    public static final MyClass stub = MethodWrapper.stubbed(MyClass.class);

    public static final Function<MyClass, String> getMyString = MethodWrapper.makeFunc(stub.getMyString());
    public static final Function<MyClass, String> getMyStringF2 = MethodWrapper.makeFunc(stub.getMyString());
    public static final Function<MyClass, Integer> getOther = MethodWrapper.makeFunc(stub.getOther());

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
