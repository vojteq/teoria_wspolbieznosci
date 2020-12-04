package lab8.zmienna_boolean;

import lab8.util.Color;
import lab8.util.ColorUtil;

import java.util.*;

public class Producer implements Runnable {
    private final int ID;
    private final Monitor monitor;
    private final Random random;
    private final int MAX_ELEMENTS;
    private final int TASKS_TO_DO;
    private final int ADDITIONAL_TASK_TIME;
    private final long START_TIME;

    public Producer(int id, Monitor monitor, int maxElements, int tasksToDO, int additionalTaskTime) {
        this.ID = id;
        this.monitor = monitor;
        random = new Random();
        this.MAX_ELEMENTS = maxElements;
        this.TASKS_TO_DO = tasksToDO;
        this.ADDITIONAL_TASK_TIME = additionalTaskTime;
        this.START_TIME = new Date().getTime();
    }

    @Override
    public void run() {
        for (int i = 0; i < TASKS_TO_DO; i++) {
            try {
                monitor.produce(Math.abs(random.nextInt() % MAX_ELEMENTS) + 1);
                Thread.sleep(ADDITIONAL_TASK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long finishTime = new Date().getTime();

        while (monitor.needProducts()) {
            try {
                monitor.produce(Math.abs(random.nextInt() % MAX_ELEMENTS) + 1);
                Thread.sleep(ADDITIONAL_TASK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ColorUtil.print(
                "producer(" + ID +"): " +
                        "done in: " + (finishTime - START_TIME) / 1000 + "s",
                Color.YELLOW);
    }

    public int getId() {
        return ID;
    }
}
