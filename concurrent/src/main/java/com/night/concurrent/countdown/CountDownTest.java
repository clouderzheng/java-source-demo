package com.night.concurrent.countdown;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownTest {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for(int i = 0; i < 10 ; i++){
            executorService.execute( () -> {
                System.out.println(atomicInteger.incrementAndGet());

                try {
                    Thread.sleep(1000);

                }catch (Exception e){
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await(5, TimeUnit.MINUTES);

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("end");
        executorService.shutdownNow();

    }
}
