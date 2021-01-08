package lab11_csp.active_object_time_mod;

import lab8.util.Color;
import lab8.util.ColorUtil;

import java.util.Date;
import java.util.Random;

public class Consumer implements Runnable{
    private final int ID;
    private final BufferProxy bufferProxy;
    private final long MAX_TIME;
    private final int OPERATION_DELAY;

    public Consumer(int id, BufferProxy bufferProxy, long maxTime, int operationDelay) {
        this.ID = id;
        this.bufferProxy = bufferProxy;
        this.MAX_TIME = maxTime;
        this.OPERATION_DELAY = operationDelay;
    }

    @Override
    public void run() {
        long startTime = new Date().getTime();
        int operationsDone = 0;
        while ((new Date().getTime() - startTime) < this.MAX_TIME) {
            FutureIntegerList futureIntegerList = bufferProxy.consumeFuture(1);
            try {
                Thread.sleep(this.OPERATION_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!futureIntegerList.isDone() && (new Date().getTime() - startTime) < this.MAX_TIME) {

            }
            operationsDone++;
        }
        System.out.println("cons done");
        long finishTime = new Date().getTime();

        ColorUtil.print("consumer(" + ID +"): " +
                        "consumptions done: " + operationsDone + " in: " + (finishTime - startTime) / 1000 + "s",
                Color.MAGENTA);
    }
}
