package com.night.thread;


/**
* 实现功能描述：线程工厂类
* @Author: zhengjingyun
* @Date: 2021/7/28
* @param
* @return
*/
public interface ThreadFactory {


    Thread newThread(Runnable r);
}
