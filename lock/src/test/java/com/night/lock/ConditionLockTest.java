package com.night.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionLockTest
{


    private static final Lock LOCK = new ReentrantLock();

    private static final Condition CONDITION = LOCK.newCondition();

    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        try {

            EXECUTORS.execute( () -> {
                try {
                    LOCK.lock();
                    CONDITION.await();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });


            Thread.sleep(5000l);
            LOCK.lock();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CONDITION.signal();
        }
        EXECUTORS.shutdownNow();
    }




}
