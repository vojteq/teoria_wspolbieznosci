package lab7.active_object;

import java.util.Random;

public class Consumer implements Runnable{
    private final BufferProxy bufferProxy;

    public Consumer(BufferProxy bufferProxy) {
        this.bufferProxy = bufferProxy;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            FutureIntegerList futureIntegerList = bufferProxy.consumeFuture(Math.abs(random.nextInt() % 10) + 1);
            while (!futureIntegerList.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                ColorUtil.print("consumed: " + futureIntegerList.get(), Color.MAGENTA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
