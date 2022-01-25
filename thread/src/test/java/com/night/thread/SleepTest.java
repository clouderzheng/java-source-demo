package com.night.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* 实现功能描述：
* @Author: zhengjingyun
* @Date: 2021/11/22
* @param
* @return
 *
 * sleep 方法是属于 Thread 类中的，sleep 过程中线程不会释放锁，只会阻塞线程，让出cpu给其他线程，
 * 但是他的监控状态依然保持着，当指定的时间到了又会自动恢复运行状态，可中断，sleep 给其他线程运行
 * 机会时不考虑线程的优先级，因此会给低优先级的线程以运行的机会
 */
public class SleepTest {


    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static Object obj = new Object();

    public static void main(String[] args) {

        executorService.execute(new Thread1());
        executorService.execute(new Thread2());

        try {
            Thread.sleep(20000);
        }catch (Exception e){
            e.printStackTrace();
        }
        executorService.shutdownNow();

    }

    static class Thread1 implements Runnable{
        @Override
        public void run() {


            synchronized (obj){
                System.out.println( " thread one start ");
                try {
                    Thread.sleep(15000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(" thread one end");
            }

        }
    }


    static class Thread2 implements Runnable{
        @Override
        public void run() {


            synchronized (obj){
                System.out.println( " thread tow start ");
                try {
                    Thread.sleep(15000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(" thread tow end");
            }

        }
    }

}

