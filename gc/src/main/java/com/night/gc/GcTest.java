package com.night.gc;

public class GcTest {

//    public static void main(String[] args) {
//
//        GcTest gcTest = new GcTest();
//
//
//        System.gc();//底层实际调用的是Runtime.getRuntime().gc();
//
//        System.out.println(" end ");
////        System.runFinalization();
//
//    }
//
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        System.out.println(" rewrite finalize ");
//    }

    public static void main(String[] args) {
        for (int i = 0; i < 47; ++i) {
            new GcObject();
            //System.out.println("runfinalize");
//            System.runFinalization();
            System.gc();
        }
    }
}
