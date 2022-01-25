package com.night.lock;


import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

    private static ExecutorService executorService =  Executors.newFixedThreadPool(4);

    public static void main(String[] args) {

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        Random random = new Random();


        for( int i = 1 ;i < 20; i++){
            executorService.execute( () -> {
                int radom = random.nextInt(10);
                System.out.println(" radom " + radom);
                if(radom % 2  == 0){
                    readWriteLock.readLock().lock();
                    System.out.println(" thread : " + Thread.currentThread().getName() + " get readlock " + System.currentTimeMillis());

                    try {
                        Thread.sleep(5000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        readWriteLock.readLock().unlock();
                        System.out.println(" thread : " + Thread.currentThread().getName() + " unlock readlock " +  System.currentTimeMillis());

                    }

                }else {

                    readWriteLock.writeLock().lock();
                    System.out.println(" thread : " + Thread.currentThread().getName() + " get writeLock " + System.currentTimeMillis());

                    try {
                        Thread.sleep(10000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        readWriteLock.writeLock().unlock();
                        System.out.println(" thread : " + Thread.currentThread().getName() + " unlock writeLock " + System.currentTimeMillis());

                    }
                }

            });
        }

//        executorService.shutdownNow();
    }
}
