package lab8.active_object;

import lab8.util.*;

import java.util.Date;
import java.util.Random;

public class Consumer implements Runnable{
    private final int ID;
    private final BufferProxy bufferProxy;
    private final int ADDITIONAL_TASK_TIME;
    private final int TASKS_TO_DO;
    private int additionalTaskCounter;
    private final long START_TIME;
    private final int MAX_ELEMS;

    public Consumer(int id, BufferProxy bufferProxy, int maxElems, int tasksToDO, int additionalTaskTime) {
        this.ID = id;
        this.bufferProxy = bufferProxy;
        this.MAX_ELEMS = maxElems;
        this.TASKS_TO_DO = tasksToDO;
        this.ADDITIONAL_TASK_TIME = additionalTaskTime;
        this.additionalTaskCounter = 0;
        this.START_TIME = new Date().getTime();
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < TASKS_TO_DO; i++) {
            FutureIntegerList futureIntegerList = bufferProxy.consumeFuture(Math.abs(random.nextInt() % MAX_ELEMS) + 1);
            while (!futureIntegerList.isDone()) {
                try {
                    Thread.sleep(ADDITIONAL_TASK_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                additionalTaskCounter++;
            }
        }

        long finishTime = new Date().getTime();

        ColorUtil.print("consumer(" + ID +"):" +
                        "done in: " + (finishTime - START_TIME) / 1000 + "s" +
                        " additional task time: " + (additionalTaskCounter * ADDITIONAL_TASK_TIME) / 1000 + "s",
                Color.MAGENTA);

        while (true){
            FutureIntegerList futureIntegerList = bufferProxy.consumeFuture(Math.abs(random.nextInt() % MAX_ELEMS) + 1);
            while (!futureIntegerList.isDone()) {
                try {
                    Thread.sleep(ADDITIONAL_TASK_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                additionalTaskCounter++;
            }
        }
    }
}
