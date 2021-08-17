package com.night.thread;

public class ThreadTest {




    public static void main(String[] args) {

        Consume consume = new Consume();
        consume.run();
        consume.start();
        consume.start();


    }

    static class Consume extends Thread{

        @Override
        public void run() {
            System.out.println("constomer thread" + this.currentThread().getName());
        }
    }
}
