package lab11_csp.active_object_time_mod;

import lab8.util.Color;
import lab8.util.ColorUtil;

import java.util.Date;
import java.util.Random;

public class Producer implements Runnable {
    private final int ID;
    private final BufferProxy bufferProxy;
    private final long MAX_TIME;
    private final int OPERATION_DELAY;

    public Producer(int id, BufferProxy bufferProxy, long maxTime, int operationDelay) {
        this.ID = id;
        this.bufferProxy = bufferProxy;
        this.MAX_TIME = maxTime;
        this.OPERATION_DELAY = operationDelay;
    }

    @Override
    public void run() {
        long startTime = new Date().getTime();
        int operationsDone = 0;
        while((new Date().getTime()) - startTime < this.MAX_TIME) {
            FutureIntegerList futureIntegerList = bufferProxy.produceFuture(1);
            try {
                Thread.sleep(OPERATION_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!futureIntegerList.isDone()) {

            }
            operationsDone++;
        }

        long finishTime = new Date().getTime();

        ColorUtil.print(
                "producer(" + ID +"): " +
                        "productions done: " + operationsDone +
                        " in: " + (finishTime - startTime) / 1000 + "s",
                Color.YELLOW);
    }
}
