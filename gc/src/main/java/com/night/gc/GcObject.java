package com.night.gc;

public class GcObject {

    public int i;
    public static int created = 0;
    public GcObject() {
        i = ++created;
    }
    @Override
    protected void finalize() throws Throwable {
        System.out.println("第" + i +"个Chair调用finalize方法");
    }
}
