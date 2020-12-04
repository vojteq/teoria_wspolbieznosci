package lab8.zmienna_boolean;

import lab8.util.Color;
import lab8.util.ColorUtil;

import java.util.Date;
import java.util.Random;

public class Consumer implements Runnable {
    private final int ID;
    private final Monitor monitor;
    private final Random random;
    private final int maxElements;
    private final int TASKS_TO_DO;
    private final int ADDITIONAL_TASK_TIME;
    private final long START_TIME;

    public Consumer(int id, Monitor monitor, int maxElements, int tasksToDO, int additionalTaskTime) {
        this.ID = id;
        this.monitor = monitor;
        this.maxElements = maxElements;
        random = new Random();
        this.TASKS_TO_DO = tasksToDO;
        this.ADDITIONAL_TASK_TIME = additionalTaskTime;
        this.START_TIME = new Date().getTime();
    }

    @Override
    public void run() {
        for (int i = 0; i < TASKS_TO_DO; i++) {
            try {
                monitor.consume(Math.abs(random.nextInt() % maxElements) + 1);
                Thread.sleep(ADDITIONAL_TASK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long finishTime = new Date().getTime();

//        monitor.dontNeedProducts();

        ColorUtil.print(
                "consumer(" + ID +"): " +
                        "done in: " + (finishTime - START_TIME) / 1000 + "s",
                Color.MAGENTA);

        while (true) {
            try {
                monitor.consume(Math.abs(random.nextInt() % maxElements) + 1);
                Thread.sleep(ADDITIONAL_TASK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return ID;
    }
}
