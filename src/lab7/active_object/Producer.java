package lab7.active_object;

import java.util.Random;

public class Producer implements Runnable {
    private final BufferProxy bufferProxy;

    public Producer(BufferProxy bufferProxy) {
        this.bufferProxy = bufferProxy;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            FutureIntegerList futureIntegerList = bufferProxy.produceFuture(Math.abs(random.nextInt() % 10) + 1);
            while (!futureIntegerList.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                ColorUtil.print("produced: " + futureIntegerList.get(), Color.YELLOW);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
