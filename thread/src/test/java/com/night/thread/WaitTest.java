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
 * wait 方法是属于 Object 类中的，wait 过程中线程会释放对象锁，只有当其他线程调用 notify
 * 才能唤醒此线程。wait 使用时必须先获取对象锁，即必须在 synchronized 修饰的代码块中使用，
 * 那么相应的 notify 方法同样必须在 synchronized 修饰的代码块中使用，如果没有在synchronized
 * 修饰的代码块中使用时运行时会抛出IllegalMonitorStateException的异常

*/
public class WaitTest {


    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static Object obj = new Object();

    public static void main(String[] args) {


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
                    obj.wait();
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
                    Thread.sleep(15000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    obj.notify();
                }
                System.out.println(" thread two end");
            }
            return " thread two success";
        }
    }

}

