package cn.kenenjoy.V2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * synchronized 关键字的使用
 * Created by hefa on 2017/11/24.
 */
public class SyncThread implements Runnable {
    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(SyncThread.class);

    private Integer value;

    private static Integer NOWVALUE;

    public SyncThread(int value) {
        this.value = value;
    }

    /**
     * 对这个类的实例化对象进行检查
     * 如果您要进行类的多个实例对象进行同步检查，那么应该对这个类的class对象进行同步检查。
     * 写法应该是：“private synchronized static void doOtherthing()”
     *
     * 当然为了对这个类（SyncThread）的class对象进行同步检查，
     * 您甚至无需在静态方法上标注synchronized关键字，
     * 而单独标注SyncThread的class的对象锁状态检查：
     * 写法如下，避免脏读：
     */
    private void doOtherthing() {
        synchronized (SyncThread.class){
            NOWVALUE = this.value;
            logger.info("当前NOWVALUE的值：" + NOWVALUE);
        }
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        Long id = currentThread.getId();
        this.doOtherthing();
    }

    public static void main(String[] args) {
        Thread syncThread1 = new Thread(new SyncThread(10));
        Thread syncThread2 = new Thread(new SyncThread(20));

        syncThread1.start();
        syncThread2.start();
    }
}
