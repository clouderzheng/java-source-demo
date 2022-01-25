package com.night.thread;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPool {


    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {


        int size  = 8;
        System.out.println(size);

        size++;
        System.out.println(size);

        ArrayList<Integer> arrayList = new ArrayList<>();

        executorService.execute( () -> {
            while (true){

                System.out.println(" arry length -> " + arrayList.size() + " result  -->  " + (arrayList.size() >=3)  );
                if(arrayList.size() >=3 ){
                    break;
                }
                arrayList.add(1);
            }
        });

        try {
            Thread.sleep(2000);

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(arrayList.toString());
        executorService.shutdownNow();
    }
}
