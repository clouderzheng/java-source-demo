package com.night.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
* 实现功能描述：
* @Author: zhengjingyun
* @Date: 2021/11/22
* @param
* @return
 *
 * 等待调用join方法的线程结束之后，程序再继续执行，一般用于等待异步线程执行完结果之后才能继续运行的场景。
 * 例如：主线程创建并启动了子线程，如果子线程中药进行大量耗时运算计算某个数据值，而主线程要取得这个数据值才能运行，这时就要用到 join 方法了
 *
 * 这里注意 join方法必须在 thread.start()之后

 */
public class JoinTest {


    private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    private static Object obj = new Object();

    public static void main(String[] args) {

        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();


        System.out.println("put task to pool ");
//        executorService.execute(thread1);
//        executorService.execute(thread2);
        BlockingQueue<Runnable> queue = executorService.getQueue();
        thread1.start();
        thread2.start();
        try{
            System.out.println(" thread join 1");
            thread1.join();
            System.out.println(" thread join 2");

            thread2.join();
            System.out.println(" thread join over");

        }catch (Exception e){
            e.printStackTrace();
        }
        executorService.shutdownNow();

    }

    static class Thread1 extends  Thread{
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


    static class Thread2  extends  Thread{
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

