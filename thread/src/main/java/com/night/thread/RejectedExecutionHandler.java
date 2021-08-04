package com.night.thread;


/**
* 实现功能描述：线程池拒绝策略
* @Author: zhengjingyun
* @Date: 2021/7/28
* @param
* @return
*/
public interface RejectedExecutionHandler {


    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);

}
