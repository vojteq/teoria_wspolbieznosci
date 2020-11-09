package lab5.zadanie;

import java.util.Random;

public class Consumer implements Runnable {
    private final int id;
    private final Monitor monitor;
    private final Random random;
    private final int maxElements;
    private int counter;        //ilosc wyciagniec z bufora

    public Consumer(int id, Monitor monitor, int maxElements) {
        counter = 0;
        this.id = id;
        this.monitor = monitor;
        this.maxElements = maxElements;
        random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.consume(this, Math.abs(random.nextInt() % maxElements) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int count() {
        return ++counter;
    }

    public int getId() {
        return id;
    }
}
