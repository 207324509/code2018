package cn.kenenjoy.V2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个线程用来发出notify请求
 * Created by hefa on 2017/11/27.
 */
public class ParentNotifyThread implements Runnable {

    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(ParentNotifyThread.class);

    /**
     * 这个对象的"钥匙"，为每个ChildNotifyThread对象所持有，
     * 模拟这个对象为所有ChildNotifyThread对象都要进行独占的现象
     */
    public static final Object WAIT_CHILEOBJECT = new Object();

    public static void main(String[] args) {
        new Thread(new ParentNotifyThread()).start();
    }


    @Override
    public void run() {
        /**
         * 3个进行WAIT_CHILEOBJECT对象独立抢占的线程，观察情况
         */
        int maxIndex = 3;
        for (int i = 0; i < maxIndex; i++) {
            ChildNotifyThread childNotify = new ChildNotifyThread();
            Thread childNotifyThread = new Thread(childNotify);
            childNotifyThread.start();
        }

        /**
         * 请在这里加debug断点
         * 以便保证ParentNotifyThread的wait()方法首先被执行了。
         *
         * 真实环境下，可以通过一个布尔型（或者其他方式）进行阻塞判断
         * 还可以使用CountDownLatch类
         */
        synchronized (ParentNotifyThread.WAIT_CHILEOBJECT) {
            /**
             * notify()方法解除阻塞状态
             * 我们并不知道将ParentNotifyThread.WAIT_CHILEOBJECT对象锁的独占权交给三个线程中的哪一个线程，
             * 这个方法只会唤醒等待对象锁的三个ChildNotifyThread类的实例对象中的一个。
             */
            ParentNotifyThread.WAIT_CHILEOBJECT.notify();
            /**
             * notifyAll()方法，唤醒所有的等待ParentNotifyThread.WAIT_CHILEOBJECT对象锁的线程。
             */
            ParentNotifyThread.WAIT_CHILEOBJECT.notifyAll();
        }

        /**
         * 没有具体的演示含义
         * 只是为了保证ParentNotifyThread不会退出
         */
        synchronized (ParentNotifyThread.class) {
            try {
                ParentNotifyThread.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}