package cn.kenenjoy.V2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java 线程对象锁简单示例
 * 用debug方式查看线程获取对象锁
 * Created by hefa on 2017/11/23.
 */
public class ThreadLock {

    /**
     * 拿来加锁的对象
     */
    private static final Object WAIT_OBJECT = new Object();

    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(ThreadLock.class);

    public static void main(String[] args) {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                // 检查'对象锁'状态
                synchronized (ThreadLock.WAIT_OBJECT) {
                    logger.info("做了一些事情。。。。。。");
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                // 检查'对象锁'状态
                synchronized (ThreadLock.WAIT_OBJECT) {
                    logger.info("做了另一些事情。。。。。。");
                }
            }
        });

        threadA.start();
        threadB.start();
        logger.info("结束。。。。。。");

    }
}
