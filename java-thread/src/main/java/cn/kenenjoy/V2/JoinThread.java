package cn.kenenjoy.V2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * join 操作
 * Created by hefa on 2017/11/28.
 */
public class JoinThread implements Runnable {

    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(JoinThread.class);

    public static void main(String[] args) {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();
        logger.info("线程" + id + "开始执行！");

        Thread joinThread = new Thread(new JoinThread());
        joinThread.start();

        try {
            /**
             * 注意：调用join方法的线程，如果接收到interrupt信号，也会抛出InterruptedException异常
             */
            joinThread.join();// 相当于调用了join(0)，一直阻塞到目标线程执行完成
//            joinThread.join(1000);// 调用线程等待1000毫秒后，无论目标线程执行是否完成，当前调用线程都会继续执行
            /**
             * 调用线程等待1000毫秒+1000000纳秒后，无论目标线程执行是否完成，当前调用线程都会继续执行
             * 其实这个纳秒只是一个参考值，且只有大于等于50万时，第二个参数才会起作用。请参考这个方法源代码
             */
//            joinThread.join(1000,500000);
            logger.info("线程" + id + "继续执行！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("线程" + id + "继续完成！");
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();
        logger.info("线程" + id + "启动成功，准备进入等待状态（5秒）");
        // 使用sleep方法，模拟这个线程执行业务代码的过程
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        // 执行到这里，说明线程被唤醒了
        logger.info("线程" + id + "执行完成！");
    }
}
