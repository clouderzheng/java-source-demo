### 线程池 
#### 线程池是典型的生产者消费者模型
- 任务队列BlockingQueue<Runnable> workQueue，用于存储需要处理的任务
- 工作线程集合HashSet<Worker> workers，继承runnable，run方法轮询消费workQueue中的任务
```$xslt
//这里注意轮询有两种方式，
//poll在时间到达返回null并且不会释放cpu
//take会等待并释放cpu
 Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                    workQueue.take();
```
- 状态标识符ctl，高位3为表示线程池状态，低位29位表示工作线程个数

#### 线程池的getActiveCount  
getActiveCount方法是遍历工作线程worker的state(AbstractQueuedSynchronizer变量)，但是此状态在lock与unlock方法之间变化，因此该方法返回的是该线程池临时的工作线程数量（正在工作的），workers && w.isLocked()
#### getPoolSize  
返回当前线程池中线程数量 workers.size()
#### 非核心线程回收  
在worker的run方法中，工作线程通过不断轮询任务队列获取任务，但是在队列为空，并且工作线程大于核心线程数就会进行回收
- 条件1
```$xslt
//线程数大于核心线程数
boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

```
- 条件2
```
//poll等待超时返回null，
Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                    workQueue.take();

timedOut = true;
```
- 条件3
```$xslt
// 返回null
 if ((wc > maximumPoolSize || (timed && timedOut))
                && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c))
                    return null;
                continue;
            }
```
- 条件4
```$xslt
//打破循环
while (task != null || (task = getTask()) != null) {

//回收线程
processWorkerExit(w, completedAbruptly);
```

#### 添加任务  
线程池的execute()方法即往任务队列添加任务，分为3种情形
- 工作线程数小于核心线程数，新增一个线程数
```$xslt
   if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
```

- 工作线程等于核心线程数，任务队列未满，加入任务队列
```$xslt
//offer方法往线程中添加一个任务，若队列中任务数量大于等于队列容量，返回false
    if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
```
- 工作线程等于核心线程数，任务队列已满，新增线程数，直到等于最大线程数
```$xslt
//请注意与第一种情况进行区别，第二个参数是false
//当为true的时候，工作线程数限制为核心线程数，为false时，核心线程数为最大线程数
addWorker(command, false)
```