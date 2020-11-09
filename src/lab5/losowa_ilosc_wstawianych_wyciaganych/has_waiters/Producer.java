package lab5.losowa_ilosc_wstawianych_wyciaganych.has_waiters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer implements Runnable {
    private final int id;
    private Monitor monitor;
    private Random random;
    private int maxElems;

    public Producer(int id, Monitor monitor, int maxElems) {
        this.id = id;
        this.monitor = monitor;
        random = new Random();
        this.maxElems = maxElems;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.produce(id, produce(Math.abs(random.nextInt() % maxElems) + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Integer> produce(int size) {
        List<Integer> products = new ArrayList<>();
        for (int i = 0; i < size; i++)
            products.add(Math.abs(random.nextInt() % 9) + 1);
        return products;
    }
}
