package cn.kenenjoy.V2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hefa on 2017/11/28.
 */
public class ChildNotifyThread implements Runnable{
    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(ChildNotifyThread.class);

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();
        logger.info("线程" + id + "启动成功，准备进入等待状态");
        synchronized (ParentNotifyThread.WAIT_CHILEOBJECT) {
            try {
                ParentNotifyThread.WAIT_CHILEOBJECT.wait();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // 执行到这里，说明线程被唤醒了
        logger.info("线程" + id + "被唤醒！");
    }
}
