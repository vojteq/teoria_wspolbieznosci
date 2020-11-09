package lab5.losowa_ilosc_wstawianych_wyciaganych.is_first;

import java.util.Random;

public class Consumer implements Runnable {
    private final int id;
    private Monitor monitor;
    private Random random;
    private int maxElems;

    public Consumer(int id, Monitor monitor, int maxElems) {
        this.id = id;
        this.monitor = monitor;
        this.maxElems = maxElems;
        random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.consume(id, Math.abs(random.nextInt() % maxElems) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
