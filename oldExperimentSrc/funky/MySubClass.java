package funky;

public class MySubClass extends MyClass {

    @Override
    public String getMyString() {
        return super.getMyString() + " plus something extra";
    }

    public static void main(String[] args) {
        MySubClass me = new MySubClass();
        me.setMyString("This is my value");
        me.setOther(456);

        printValues(me);
    }
}
