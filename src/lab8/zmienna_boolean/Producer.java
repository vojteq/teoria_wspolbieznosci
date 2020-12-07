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
    private int additionalTask;

    public Producer(int id, Monitor monitor, int maxElements, int tasksToDO, int additionalTask) {
        this.ID = id;
        this.monitor = monitor;
        random = new Random();
        this.MAX_ELEMENTS = maxElements;
        this.TASKS_TO_DO = tasksToDO;
        this.ADDITIONAL_TASK_TIME = 10;
        this.additionalTask = additionalTask;
    }

    @Override
    public void run() {
        long startTime = new Date().getTime();
        for (int i = 0; i < TASKS_TO_DO; i++) {
            try {
                monitor.produce(Math.abs(random.nextInt() % MAX_ELEMENTS) + 1);
                if (additionalTask > 0) {
                    Thread.sleep(ADDITIONAL_TASK_TIME);
                    additionalTask--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while(additionalTask > 0) {
            try {
                Thread.sleep(ADDITIONAL_TASK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            additionalTask--;
        }

        long finishTime = new Date().getTime();

        ColorUtil.print(
                "producer(" + ID +"): " +
                        "done in: " + (finishTime - startTime) / 1000 + "s",
                Color.YELLOW);

        while (true) {
            try {
                monitor.produce(Math.abs(random.nextInt() % MAX_ELEMENTS) + 1);
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
