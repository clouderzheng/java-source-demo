package com.night.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
* 实现功能描述：
* @Author: zhengjingyun
* @Date: 2021/11/22
* @param
* @return
 *
 * 和 sleep 一样都是 Thread 类的方法，都是暂停当前正在执行的线程对象，不会释放资源锁，
 * 和 sleep 不同的是 yield方法并不会让线程进入阻塞状态，而是让线程重回就绪状态，它只需
 * 要等待重新获取CPU执行时间，所以执行yield()的线程有可能在进入到可执行状态后马上又被执
 * 行。还有一点和 sleep 不同的是 yield 方法只能使同优先级或更高优先级的线程有执行的机会

*/
public class YieldTest {


    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static Object obj = new Object();

    public static void main(String[] args) {

        System.out.println(Math.abs("cdd0308e81e7d6438e9f632394ce4516:11:27".hashCode()) % 10 + 1 );


        List<FutureTask<String>> taskList = new ArrayList<FutureTask<String>>();

        FutureTask<String> task1 = new FutureTask<String>(new Thread1());

        taskList.add(task1);

        FutureTask<String> task2 = new FutureTask<String>(new Thread2());

        taskList.add(task2);

        System.out.println(" task put in pool");
        executorService.execute(task1);
        executorService.execute(task2);


        List<String> result = new ArrayList<String>();

        System.out.println(" poll result");

        taskList.forEach( task -> {
            try {
                System.out.println(task.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println("shut down pool");

        executorService.shutdownNow();

    }

    static class Thread1 implements Callable<String> {

        @Override
        public String call() throws Exception {
            synchronized (obj){
                System.out.println( " thread one start ");
                try {
                    Thread.yield();
                    System.out.println("thread one retry execute  ");
                    Thread.sleep(10000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(" thread one end");
            }
            return " thread one success";
        }
    }


    static class Thread2 implements Callable<String> {
        @Override
        public String call() throws Exception {
            synchronized (obj){
                System.out.println( " thread two start ");
                try {
                    Thread.yield();
                    System.out.println("thread two retry execute  ");
                    Thread.sleep(10000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(" thread two end");
            }
            return " thread two success";
        }
    }

}

