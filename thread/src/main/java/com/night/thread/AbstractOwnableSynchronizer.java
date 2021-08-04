package com.night.thread;

public abstract class AbstractOwnableSynchronizer
        implements java.io.Serializable {

    /**
     * 当前独占模式的拥有者
     */

    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }



}