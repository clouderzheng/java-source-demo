
### thread-method info

#### 方法解析
- yield  
yield 即 "谦让"，也是 Thread 类的方法。它让掉当前线程 CPU 的时间片，使正在运行中的线程重新变成就绪状态，并重新竞争 CPU 的调度权。它可能会获取到，也有可能被其他线程获取到。
- run  
run方法就是一个普通方法，即在单线程中调用run方法，是串行的
- start  
start方法调用后，将被加入到一个 ThreadGroup中，最终通过start0()方法调用run方法，注意：start方法只能调用一次，调用之后，threadStatus不为0，再次调用抛出IllegalThreadStateException异常

#### yield 和 sleep 的异同   
1）yield, sleep 都能暂停当前线程，sleep 可以指定具体休眠的时间，而 yield 则依赖 CPU 的时间片划分。

2）yield, sleep 两个在暂停过程中，如已经持有锁，则都不会释放锁资源。

3）yield 不能被中断，而 sleep 则可以接受中断。

