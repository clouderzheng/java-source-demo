package com.night.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    final Lock lock = new ReentrantLock();


    final Condition condition = lock.newCondition();


    public Lock getLock(){
        return lock;
    }


    public static void main(String[] args) {

        ConditionTest conditionTest = new ConditionTest();
        Producer producer = conditionTest.new Producer();
        Consumer consumer = conditionTest.new Consumer();

        System.out.println(producer.getLock() == consumer.getLock());
        consumer.start();
        producer.start();
//        consumer.run();
//        producer.run();
    }


    class Consumer extends  Thread{


        public Lock getLock(){
            return lock;
        }

        @Override
        public void run() {
            consume();
        }
        private void consume(){

            try {
                lock.lock();
                System.out.println("wait for a signal " + this.currentThread().getName() +  " "  + System.currentTimeMillis() );
                Thread.sleep(5000);
                condition.await(5, TimeUnit.SECONDS);
                System.out.println("condition.await() begin ");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                System.out.println(" get a signal " + this.currentThread().getName() +  " "  + System.currentTimeMillis() );
                lock.unlock();
                try {
                    Thread.sleep(5000);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    class Producer extends Thread{

        public Lock getLock(){
            return lock;
        }
        @Override
        public void run() {
            produce();
        }

        private void produce(){
            try {

                lock.lock();
                System.out.println( " get lock " + this.currentThread().getName() +  " "  + System.currentTimeMillis() );
                Thread.sleep(5000);

                condition.signal();
                System.out.println(" send a singal " + this.currentThread().getName() +  " "  + System.currentTimeMillis() );
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}
