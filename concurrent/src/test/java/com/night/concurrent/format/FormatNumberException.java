package com.night.concurrent.format;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormatNumberException {


    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
//    @Test
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int i = 0 ; i < 2;i++){
            executorService.execute( () -> {
                try {
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SIMPLE_DATE_FORMAT.parse("2021-08-11");
                    System.out.println(Thread.currentThread().getName()+" over");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();

    }
}
