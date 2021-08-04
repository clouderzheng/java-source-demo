package com.night.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,5,5, TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>());

        for(int i = 0 ; i < 10;i++){
            final int task = i;
            threadPoolExecutor.execute( () -> {
                System.out.println("current task " + task + " current thread name " + Thread.currentThread().getName());

                System.out.println(threadPoolExecutor.getActiveCount());
            });
        }

    }
}
