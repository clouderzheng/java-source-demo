package com.night.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    final static Lock lock = new ReentrantLock();

    public static void main(String[] args) {



//        lock.lock();
//
//        try {
//
//            System.out.println("get lock success");
//            lock.lock();
//            System.out.println("get second lock");
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            lock.unlock();
//            System.out.println( " unlock success");
//        }

        ExecutorService executorService = Executors.newCachedThreadPool();


        AtomicInteger atomicInteger = new AtomicInteger(0);
        for(int i = 0 ; i < 3; i++){
            executorService.execute( () -> {
                System.out.println(Thread.currentThread().getName() + "begin to get lock "  + System.currentTimeMillis());
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "have getted lock "  + System.currentTimeMillis());
                try {
                    if(atomicInteger.getAndIncrement() == 0){
                        System.out.println("第一次进入睡眠10秒 "  + System.currentTimeMillis() + Thread.currentThread().getName());
                        Thread.sleep(500000);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }

            });
        }
        executorService.shutdown();
    }
}
