package com.night.thread;


/**
* 实现功能描述 提交任务到线程池
* @Author: zhengjingyun
* @Date: 2021/7/28
* @param
* @return
*/
public interface Executor {



    /**
    * 实现功能描述：提交任务
    * @Author: zhengjingyun
    * @Date: 2021/7/28
    * @param
    * @return
    */
    void execute(Runnable command);

}
