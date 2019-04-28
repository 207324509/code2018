package cn.kenenjoy.v1;

/**
 * Created by hefa on 2018/12/11.
 */
public class VolatileDemo {
    private static volatile boolean isOver = false;

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                while (!isOver) ;
                long endTime = System.currentTimeMillis();
                System.out.println(endTime-startTime);
            }
        });
        thread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isOver = true;
    }
}
