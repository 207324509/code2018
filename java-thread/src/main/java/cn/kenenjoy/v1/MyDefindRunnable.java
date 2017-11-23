package cn.kenenjoy.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java中最简单的线程示例，实现Runnable接口
 * Created by hefa on 2017/11/22.
 */
public class MyDefindRunnable implements Runnable {
    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(MyDefindRunnable.class);

    public void run() {
        // 获取当前线程ID
        Long threadId = Thread.currentThread().getId();
        logger.info("线程（" + threadId + "）做了一些事情，然后结束了。");
    }

    public static void main(String[] args){
        new Thread(new MyDefindRunnable()).start();
    }
}
