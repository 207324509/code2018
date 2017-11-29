package cn.kenenjoy.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hefa on 2017/11/29.
 */
public class PoolThreadSimple {

    private static Logger logger = LoggerFactory.getLogger(PoolThreadSimple.class);

    public static void main(String[] args) {
        /**
         * corePoolSize：核心大小，线程池初始化的时候，就会有这么大
         * maximumPoolSize：线程池最大线程数
         * keepAliveTime：如果当前线程池中线程数大于corePoolSize，多余的线程在等待keepAliveTime后没有任务，它就会被回收。
         * TimeUnit：等待时间keepAliveTimed的单位
         * workQueue：等待队列。
         */
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 10, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());

        // 不常用设置
//        poolExecutor.allowCoreThreadTimeOut(true );// 在等待keepAliveTime时间后全部进行回收，包括"核心线程"在内
//        poolExecutor.prestartAllCoreThreads();// 在线程池创建时，还没有收到任何任务的情况下可以先创建符合corePoolSize参数值的线程数

        for (int index = 0; index < 10; index++) {
            poolExecutor.submit(new PoolThreadSimple.TestRunnable(index));
        }

        // 没有特殊含义，只是为了保证main线程不会退出
        synchronized (poolExecutor) {
            try {
                poolExecutor.wait();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 测试用的线程
     */
    private static class TestRunnable implements Runnable {

        /**
         * logback日志，如果没有则使用System.out
         */
        private static Logger logger = LoggerFactory.getLogger(TestRunnable.class);

        /**
         * 记录任务的唯一编号，这样在日志中好做识别
         */
        private Integer index;

        public Integer getIndex() {
            return index;
        }

        public TestRunnable(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            /**
             * 线程中，就只做一件事情：
             * 等待60秒钟的事件，以便模拟业务操作过程
             */
            Thread currentThread = Thread.currentThread();
            logger.info("线程：" + currentThread.getId() + "中的任务（" + this.index + "）开始执行！");
            synchronized (currentThread) {
                try {
                    currentThread.wait(6000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            logger.info("线程：" + currentThread.getId() + "中的任务（" + this.index + "）开始完成！");
        }
    }
}
