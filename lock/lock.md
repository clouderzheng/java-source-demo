### lock info

#### lock创建
```$xslt

create lock:
Lock lock = new ReentrantLock();
上面的无参构造默认调用创建了非公平锁
  public ReentrantLock() {
        sync = new NonfairSync();
    }
 这里只是创建了一个非公平锁对象，继承关系
 NonfairSync extends Sync
 Sync extends AbstractQueuedSynchronizer 
 AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer
```

### lock使用

```
lock.lock(); //从这里开始真正的逻辑，这里以ReentrantLock为例
 public void lock() {
        sync.lock(); // 调用sync的lock方法
    }
    
    static final class NonfairSync extends Sync {
      
      //实际真正执行的是NonfairSync的lock方法
        final void lock() {
        //这里进行原子化操作比较，判断当前获取锁状态
        //这个方法的逻辑，获取该对象的固定偏移量，即锁的值，是否预期与修改一致来判断是否能获取锁
        //修改成功，即获取锁成功，把当前线程与锁绑定，表示当前线程独占锁
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
            //否则继续下述逻辑
                acquire(1);
        }
    }
    //判断是否同一线程，同一线程可实现重入，否则新建一个等待节点，挂载在等待队列中
        public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    
    新建一个等待队列，快速加入等待队列
    enq()方法是一个自旋函数，保证上面快速添加失败之后的补救
       private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
    线程休眠，等待唤醒
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
            //获取当前节点的上个节点是否头节点，如果是头节点，又刚好获取锁，手动释放头节点，并且自身替代头节点
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                //修改上级节点状态 pred.waitStatus = Node.SIGNAL
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

 
    
    下述是非公平的实现
    final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            //这里针对锁被释放时候的一次尝试获取
            if (c == 0) {
                if (compareAndSetState(0, acquires)) { 
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {//这里是同一线程的可重入判断
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    
     
```



````
````

###  ext

```
获取某个字段相对Java对象的“起始地址”的偏移量
unsafe.objectFieldOffset  
这里直接操作的内存，获取相对“起始地址”固定变量（固定偏移量决定）的值
```

### 公平与非公平锁

``` 
实现方式：
公平锁相对于非公平锁多了一个判断条件
在head != tail的情况下
h != t  true
(s = h.next ) == null  false
最终转换
true && ( false || s.thread != Thread.currentThread())
结果取决于s.thread != Thread.currentThread()这段代码
这也就与 head.next.thread线程关联上了，根据链表添加规则，head.next一定是排队最久的，所以就实现了公平逻辑
   public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }
    
非公平锁是指新来线程有很大的几率抢占到锁，从而造成插队行为，但是当线程未抢占到锁，然后排队到等待队列之后，流程就与公平锁一致了，都是按照队列顺序依次处理。
```