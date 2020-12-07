package lab8.active_object;

import lab8.util.*;

import java.util.Date;
import java.util.Random;

public class Producer implements Runnable {
    private final int ID;
    private final BufferProxy bufferProxy;
    private final int ADDITIONAL_TASK_TIME;
    private int additionalTask;
    private final int TASKS_TO_DO;
    private final int MAX_ELEMS;

    public Producer(int id, BufferProxy bufferProxy, int maxElems, int tasksToDO, int additionalTask) {
        this.ID = id;
        this.bufferProxy = bufferProxy;
        this.MAX_ELEMS = maxElems;
        this.TASKS_TO_DO = tasksToDO;
        this.ADDITIONAL_TASK_TIME = 10;
        this.additionalTask = additionalTask;
    }

    @Override
    public void run() {
        long startTime = new Date().getTime();
        Random random = new Random();
        for (int i = 0; i < TASKS_TO_DO; i++) {
            FutureIntegerList futureIntegerList = bufferProxy.produceFuture(Math.abs(random.nextInt() % MAX_ELEMS) + 1);
            while (!futureIntegerList.isDone()) {
                if (additionalTask > 0) {
                    try {
                        Thread.sleep(ADDITIONAL_TASK_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    additionalTask--;
                }
            }
        }

    while (additionalTask > 0) {
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
            FutureIntegerList futureIntegerList = bufferProxy.produceFuture(Math.abs(random.nextInt() % 10) + 1);
            try {
                Thread.sleep(ADDITIONAL_TASK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
