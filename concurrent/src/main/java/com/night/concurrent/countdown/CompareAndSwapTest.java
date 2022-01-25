package com.night.concurrent.countdown;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CompareAndSwapTest {

//    private static final long stateOffset;

    private static int state = 8;

    private static final Unsafe unsafe = Unsafe.getUnsafe();
//
//
//    static {
//        try {
//            stateOffset = unsafe.objectFieldOffset
//                    (CompareAndSwapTest.class.getDeclaredField("state"));
//
//        } catch (Exception ex) { throw new Error(ex); }
//    }

    public static void main(String[] args) {
//        String property = System.getProperty("java.class.path");
//        System.out.println(property);

        try {

            System.out.println(System.getProperty("sun.boot.class.path"));
            Class<?> aClass = Class.forName("com.night.concurrent.countdown.CompareAndSwapTest");
            System.out.println(aClass.getClassLoader());
            Class<?> unsafeObject = Class.forName("sun.misc.Unsafe");
            Method getUnsafe = unsafeObject.getDeclaredMethod("getUnsafe");
            Unsafe unsafe = (Unsafe) getUnsafe.invoke(unsafeObject);
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            theUnsafe.getClass();

        }catch (Exception e){
            e.printStackTrace();
        }

//        boolean b = unsafe.compareAndSwapInt(CompareAndSwapTest.class, stateOffset, state, 7);
//        System.out.println(" swap result - > " + b);
    }
}
