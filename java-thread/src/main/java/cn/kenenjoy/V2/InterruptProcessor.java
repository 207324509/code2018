package cn.kenenjoy.V2;

/**
 * Created by hefa on 2017/11/28.
 */
public class InterruptProcessor {
    public static void main(String[] args) {
        /**
         * thread.isInterrupted()和Thread.interrupted()区别
         * 在Java的线程基本操作方法中，有两种方式获取当前线程的isInterrupted属性。
         * 一种是对象方法thread.isInterrupted()，另一种是Thread类的静态方法Thread.interrupted()
         * 这两个方法看似相同，实际上是有区别的。
         * 两者都是调用JNI底层的private native boolean isInterrupted(boolean ClearInterrupted)方法，但是区别在于这个ClearInterrupted参数
         * 前者传入false，后者传入true。
         * 当某个线程的isInterrupted属性成功被设置为true后，如果你使用对象方法thread.isInterrupted()获取值，无论多少次返回值都是true。
         * 如果是你使用静态方法Thread.interrupted()获取值，只有第一次获取的结果是true，随后线程的isInterrupted属性将被恢复成false，
         * 无论后续使用Thread.interrupted()还是使用thread.isInterrupted()调用，获取的结果都是false。
         */
        // thread one线程
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread currentThread = Thread.currentThread();
                /**
                 * 并不是线程收到interrupt信号就会立即响应
                 * 线程需要检查自生状态是否正常，然后决定下一步怎么走。
                 */
                while (!currentThread.isInterrupted()) { // 返回为true
                    /**
                     * 这里打印一句话，说明循环一直在运行
                     * 但是正式系统中不建议这样写代码，因为没有中断（wait、sleep）的无限循环是非常耗费CPU资源
                     */
                    System.out.println("Thread One 一直在运行！");
                }

                System.out.println("thread one 正常结束！" + currentThread.isInterrupted());
            }
        });

        // thread two线程
        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread currentThread = Thread.currentThread();
                while (!currentThread.isInterrupted()){
                    synchronized (currentThread){
                        try {
                            currentThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.out);
                            System.out.println("thread two 由于中断信号，异常结束！" + currentThread.isInterrupted());
                            return;
                        }

                    }
                }
                System.out.println("thread two 正常结束！");
            }
        });

        threadOne.start();
        threadTwo.start();
        /**
         * 在这里打上debug断点，以保证threadOne和threadTwo完成了启动
         */
        System.out.println("两个线程正常运行，现在开发发出中断信号");
        threadOne.interrupt();
        threadTwo.interrupt();

    }
}
