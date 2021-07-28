package com.night.thread;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutor extends AbstractExecutorService {

    /**
     * 核心线程数
     */
    private volatile int corePoolSize;

    /**
     * 最大线程数
     */
    private volatile int maximumPoolSize;


    /**
     * 非核心线程数超时回收时间
     */
    private volatile long keepAliveTime;


    /**·
     * 线程拒绝策略
     */
    private volatile RejectedExecutionHandler handler;


    /**
     * 线程池的控制状态  高三位  线程状态 后29位  线程数量
     */
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    /**
     * 表示线程个数的位数 29位
     */
    private static final int COUNT_BITS = Integer.SIZE - 3;

    /**
     * 最大线程个数  536870911
     * 1 << COUNT_BITS
     * 1 << 29
     * 00010000000000000000000000000000
     * 00001111111111111111111111111111
     */
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    /**
     * 高位表示的线程状态
     */

    /**
     * 11100000000000000000000000000000
     *
     * 111
     */
    private static final int RUNNING    = -1 << COUNT_BITS;

    /**
     * 00000000000000000000000000000000
     * 000
     */
    private static final int SHUTDOWN   =  0 << COUNT_BITS;

    /**
     * 00100000000000000000000000000000
     * 001
     */
    private static final int STOP       =  1 << COUNT_BITS;

    /**
     * 01000000000000000000000000000000
     * 010
     */
    private static final int TIDYING    =  2 << COUNT_BITS;

    /**
     * 01100000000000000000000000000000
     * 011
     */
    private static final int TERMINATED =  3 << COUNT_BITS;


    /**
    * 实现功能描述：计算线程池  信息  线程池
    * @Author: zhengjingyun
    * @Date: 2021/7/28
    * @param
    * @return
    */
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    /**
     * 任务队列
     */
    private final BlockingQueue<Runnable> workQueue;


    private volatile ThreadFactory threadFactory;




    private static final RejectedExecutionHandler defaultHandler =
            new AbortPolicy();


    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                Executors.defaultThreadFactory(), defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
                maximumPoolSize <= 0 ||
                maximumPoolSize < corePoolSize ||
                keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();

        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    @Override
    public void execute(Runnable command) {
        int c = ctl.get();

    }

    public static class AbortPolicy implements RejectedExecutionHandler {
        /**
         * Creates an {@code AbortPolicy}.
         */
        public AbortPolicy() { }


        /**
        * 实现功能描述：直接抛出异常拒绝
        * @Author: zhengjingyun
        * @Date: 2021/7/28
        * @param
        * @return
        */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }

}
