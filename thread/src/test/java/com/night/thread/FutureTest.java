package com.night.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureTest {


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
                    Thread.sleep(15000);
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
                }
                System.out.println(" thread two end");
            }
            return " thread two success";
        }
    }

}

