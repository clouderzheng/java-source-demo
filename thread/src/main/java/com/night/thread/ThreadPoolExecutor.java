package com.night.thread;


import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

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


    /**
     * ·
     * 线程拒绝策略
     */
    private volatile RejectedExecutionHandler handler;

    private final ReentrantLock mainLock = new ReentrantLock();


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
     * 00010000000000000000000000000000 - 1
     * 00001111111111111111111111111111
     */
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    /**
     * 高位表示的线程状态
     */

    /**
     * 11100000000000000000000000000000
     * (1) 状态说明：线程池处在RUNNING状态时，能够接收新任务，以及对已添加的任务进行处理。
     * (02) 状态切换：线程池的初始化状态是RUNNING。换句话说，线程池被一旦被创建，就处于RUNNING状态，并且线程池中的任务数为
     * <p>
     * 111
     */
    private static final int RUNNING = -1 << COUNT_BITS;

    /**
     * 00000000000000000000000000000000
     * 000
     * (1) 状态说明：线程池处在SHUTDOWN状态时，不接收新任务，但能处理已添加的任务。
     * (2) 状态切换：调用线程池的shutdown()接口时，线程池由RUNNING -> SHUTDOWN
     */
    private static final int SHUTDOWN = 0 << COUNT_BITS;

    /**
     * 00100000000000000000000000000000
     * 001
     * (1) 状态说明：线程池处在STOP状态时，不接收新任务，不处理已添加的任务，并且会中断正在处理的任务。
     * (2) 状态切换：调用线程池的shutdownNow()接口时，线程池由(RUNNING or SHUTDOWN ) -> STOP。
     */
    private static final int STOP = 1 << COUNT_BITS;

    /**
     * 01000000000000000000000000000000
     * 010
     * (1) 状态说明：当所有的任务已终止，ctl记录的”任务数量”为0，线程池会变为TIDYING状态。当线程池变为TIDYING状态时，会执行钩子函数terminated()。terminated()在ThreadPoolExecutor类中是空的，若用户想在线程池变为TIDYING时，进行相应的处理；可以通过重载terminated()函数来实现。
     * (2) 状态切换：当线程池在SHUTDOWN状态下，阻塞队列为空并且线程池中执行的任务也为空时，就会由 SHUTDOWN -> TIDYING。
     * 当线程池在STOP状态下，线程池中执行的任务为空时，就会由STOP -> TIDYING
     */
    private static final int TIDYING = 2 << COUNT_BITS;

    /**
     * 01100000000000000000000000000000
     * 011
     * (1) 状态说明：线程池彻底终止，就变成TERMINATED状态。
     * (2) 状态切换：线程池处在TIDYING状态时，执行完terminated()之后，就会由 TIDYING -> TERMINATED。
     */
    private static final int TERMINATED = 3 << COUNT_BITS;


    /**
     * 工作线程
     */
    private final HashSet<Worker> workers = new HashSet<Worker>();

    private static final boolean ONLY_ONE = true;



    /**
     * 实现功能描述：计算线程池  信息  线程池
     *
     * @param
     * @return
     * @Author: zhengjingyun
     * @Date: 2021/7/28
     */
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    /**
     * 任务队列
     */
    private final BlockingQueue<Runnable> workQueue;


    private volatile ThreadFactory threadFactory;

    private int largestPoolSize;


    /**
     * 计算工作线程数量
     *
     * @param c
     * @return
     */
    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    /**
     * CAPACITY    0001111111111111111111111111111
     *  ~CAPACITY  11100000000000000000000000000000
     * @param c
     * @return
     */
    private static int runStateOf(int c)     { return c & ~CAPACITY; }




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
                keepAliveTime < 0){
            throw new IllegalArgumentException();
        }
        if (workQueue == null || threadFactory == null || handler == null){
            throw new NullPointerException();
        }

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

        //当前线程数量低于核心线程数
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true)){
                return;
            }
            c = ctl.get();
        }

    }

    private boolean addWorker(Runnable firstTask, boolean core) {

        retry:
        for (;;){
            //获取当前线程池参数值
            int c = ctl.get();
            int rs = runStateOf(c);

            //判断线程状态
            if(rs >= SHUTDOWN && !( rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty())){
                return false;
            }
            for(;;){
                int wc = workerCountOf(c);
                //判断当前线程数是否超过限制或者核心线程数或者最大线程数
                if(wc >= CAPACITY || wc > (core ? corePoolSize : maximumPoolSize)){
                    return false;
                }
                //这里是阻止并发
                if(compareAndIncrementWorkerCount(c)){
                    break retry;
                }
                c = ctl.get();
                //线程池状态前后一致
                if(runStateOf(c) != rs){
                    continue retry;
                }

            }
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {

            w = new Worker(firstTask);
            final  Thread t = w.thread;
            if(t != null){
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    int rs  = runStateOf(ctl.get());
                    if(rs < SHUTDOWN || (rs == SHUTDOWN && firstTask == null)){

                        //测试此线程是否处于活动状态。如果线程已启动但尚未死亡，则该线程是活动的。
                        //todo 为什么要判断
                        if(t.isAlive()){
                            throw  new IllegalThreadStateException();
                        }
                        workers.add(w);
                        int s = workers.size();
                        if(s > largestPoolSize){
                            largestPoolSize = s;
                        }
                        workerAdded = true;
                    }

                }finally {
                    mainLock.unlock();
                }
                if(workerAdded){
                    t.start();
                    workerStarted = true;
                }
            }
        }finally {
            if(!workerStarted){
                addWorkerFailed(w);
            }
        }
        return workerStarted;
    }


    private void addWorkerFailed(Worker w) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (w != null){
                workers.remove(w);
            }
            decrementWorkerCount();
            tryTerminate();
        } finally {
            mainLock.unlock();
        }
    }


    /**
    * 实现功能描述：停止线程
    * @Author: zhengjingyun
    * @Date: 2021/7/29
    * @param
    * @return
    */
    final void tryTerminate() {
//        for (;;) {
//            int c = ctl.get();
//            if (isRunning(c) ||
//                    runStateAtLeast(c, TIDYING) ||
//                    (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty())){
//                return;
//            }
//            //线程数不为0
//            if (workerCountOf(c) != 0) { // Eligible to terminate
//                interruptIdleWorkers(ONLY_ONE);
//                return;
//            }
//
//            final ReentrantLock mainLock = this.mainLock;
//            mainLock.lock();
//            try {
//                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
//                    try {
//                        terminated();
//                    } finally {
//                        ctl.set(ctlOf(TERMINATED, 0));
//                        termination.signalAll();
//                    }
//                    return;
//                }
//            } finally {
//                mainLock.unlock();
//            }
//            // else retry on failed CAS
//        }
    }



    /**
    * 实现功能描述：中断当前等待中的线程
    * @Author: zhengjingyun
    * @Date: 2021/7/30
    * @param
    * @return
    */
    private void interruptIdleWorkers(boolean onlyOne) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers) {
                Thread t = w.thread;
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        t.interrupt();
                    } catch (SecurityException ignore) {
                    } finally {
                        w.unlock();
                    }
                }
                if (onlyOne){
                    break;
                }
            }
        } finally {
            mainLock.unlock();
        }
    }

    /**
    * 实现功能描述：线程是否处于即将关闭状态
    * @Author: zhengjingyun
    * @Date: 2021/7/30
    * @param
    * @return
    */
    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }



    /**
    * 实现功能描述：线程运行中
    * @Author: zhengjingyun
    * @Date: 2021/7/30
    * @param
    * @return
    */
    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }
    /**
    * 实现功能描述：原子更新线程数量
    * @Author: zhengjingyun
    * @Date: 2021/7/29
    * @param
    * @return
    */
    private void decrementWorkerCount() {
        do {} while (! compareAndDecrementWorkerCount(ctl.get()));
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }
    /**
    * 实现功能描述：原子加一
    * @Author: zhengjingyun
    * @Date: 2021/7/29
    * @param
    * @return
    */
    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    public static class AbortPolicy implements RejectedExecutionHandler {
        /**
         * Creates an {@code AbortPolicy}.
         */
        public AbortPolicy() {
        }


        /**
         * 实现功能描述：直接抛出异常拒绝
         *
         * @param
         * @return
         * @Author: zhengjingyun
         * @Date: 2021/7/28
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }
    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    private final class Worker
            extends AbstractQueuedSynchronizer
            implements Runnable{

        Runnable firstTask;

        public boolean tryLock()  { return tryAcquire(1); }

        @Override
        protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }



        public void unlock()      { release(1); }


        /**
         * 工作线程
         */
        final Thread thread;

        private volatile int state;
        Worker(Runnable firstTask) {
            setState(-1); // inhibit interrupts until runWorker
            this.firstTask = firstTask;
            //新建工作线程
            this.thread = getThreadFactory().newThread(this);
        }

        protected final void setState(int newState) {
            state = newState;
        }


        @Override
        public void run() {

        }


    }
}
