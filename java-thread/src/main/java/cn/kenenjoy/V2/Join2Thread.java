package cn.kenenjoy.V2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sleep操作
 * Created by hefa on 2017/11/28.
 */
public class Join2Thread implements Runnable {

    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(Join2Thread.class);

    public static void main(String[] args) {
        /**
         * sleep方法不像wait方法的执行效果那样，sleep方法不会释放当前线程所占用的对象锁独占模式。
         * joinThread1线程拿到对象锁的独占权后，joinThread2线程会一直等待获得Join2Thread对象锁的钥匙。
         */
        Thread joinThread1 = new Thread(new Join2Thread());
        joinThread1.start();
        Thread joinThread2 = new Thread(new Join2Thread());
        joinThread2.start();


        /**
         * joinThread调用sleep方法后currentThread进入阻塞状态
         */
//        Thread currentThread = Thread.currentThread();
//        Thread joinThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                logger.info("执行中！");
//            }
//        });
//        joinThread.start();
//        try {
//            joinThread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        synchronized (Join2Thread.class){
//            try {
//                Join2Thread.class.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void run() {
        // 使用sleep方法，模型这个线程执行业务代码过程
        synchronized (Join2Thread.class){
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
