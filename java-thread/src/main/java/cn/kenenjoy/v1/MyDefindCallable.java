package cn.kenenjoy.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by hefa on 2018/12/7.
 */
public class MyDefindCallable {
    /**
     * logback日志，如果没有则使用System.out
     */
    private static Logger logger = LoggerFactory.getLogger(MyDefindCallable.class);

    private static ExecutorService service = Executors.newSingleThreadExecutor();

    private static Future<String> future = service.submit(new Callable() {
        @Override
        public String call() throws Exception {
            // 获取当前线程ID
            Long threadId = Thread.currentThread().getId();
            logger.info("线程（" + threadId + "）做了一些事情，然后结束了。");
            return "通过实现Callable接口";
        }
    });

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String result = future.get();
        logger.info(result);
    }
}
