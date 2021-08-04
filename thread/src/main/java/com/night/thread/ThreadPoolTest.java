package com.night.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,5,5, TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>());

        threadPoolExecutor.execute( () -> System.out.println("ok"));
    }
}
