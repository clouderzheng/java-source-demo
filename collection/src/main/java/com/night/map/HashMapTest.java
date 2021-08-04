package com.night.map;

import com.night.map.HashMap;
import com.night.map.Map;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HashMapTest
{

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(5));
//        threadPoolExecutor.prestartCoreThread();
//        threadPoolExecutor.prestartAllCoreThreads();
        System.out.println(threadPoolExecutor.getActiveCount());

        ThreadPoolExecutor threadPoolExecutor1 = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1));
        threadPoolExecutor1.execute( () -> {
            for(;;){
                try {
                    Thread.sleep(1000);
//                    System.out.println(" threadPoolExecutor current active thread " + threadPoolExecutor.getQueue().size());
//                    System.out.println(" threadPoolExecutor current task " + threadPoolExecutor.getQueue().size());
                    System.out.println(" threadPoolExecutor current thread " + threadPoolExecutor.getPoolSize());

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });




        for (int i = 0; i < 10; i++) {
//            try {
//                Thread.sleep(500);
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            final int task = i;
            threadPoolExecutor.execute(() -> {
                System.out.println("current task " + task + " current thread name " + Thread.currentThread().getName() + " active count " + threadPoolExecutor.getActiveCount()) ;
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();

                }

            });
        }
        System.out.println(" main " + threadPoolExecutor.getActiveCount());

//        try {
//            Thread.sleep(30000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("  after main " + threadPoolExecutor.getActiveCount());
//
//        threadPoolExecutor.shutdown();
    }

}
