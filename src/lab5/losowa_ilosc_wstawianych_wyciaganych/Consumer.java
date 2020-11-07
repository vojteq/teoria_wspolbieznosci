package lab5.losowa_ilosc_wstawianych_wyciaganych;

import java.util.Random;

public class Consumer implements Runnable {
    private Monitor monitor;
    private Random random;
    private int maxElems;

    public Consumer(Monitor monitor, int maxElems) {
        this.monitor = monitor;
        random = new Random();
        this.maxElems = maxElems;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.consume(random.nextInt() % maxElems);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
