package com.night.concurrent.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class FormatTest {

    private static final int EXECUTE_COUNT = 100;

    private static final int THREAD_COUNT = 20;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {

        final Semaphore semaphore = new Semaphore(THREAD_COUNT);
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);

        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int  i = 0 ; i < EXECUTE_COUNT ; i ++){
            executorService.execute( () -> {
                try {
                    semaphore.acquire();

                    try {
                        simpleDateFormat.parse("2021-08-11");
                        simpleDateFormat.format(new Date());
                    } catch (ParseException e) {

                        System.out.println("线程 "+ Thread.currentThread().getName() +" 格式化日期失败");
                        e.printStackTrace();
                        System.exit(1);
                    }
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println("信号量发生错误");
                    e.printStackTrace();
                    System.exit(1);
                }
                countDownLatch.countDown();
            });

        }


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println("所有线程格式化完毕");

    }
}
