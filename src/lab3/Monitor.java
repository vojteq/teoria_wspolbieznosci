package lab3;

import java.util.ArrayList;
import java.util.Random;

public class Monitor {
    private ArrayList<Integer> values;
    private int maxSize;
    private Random random;

    public Monitor(int maxSize) {
        values = new ArrayList<>();
        random = new Random();
        this.maxSize = maxSize;
    }

    public synchronized void produce() throws InterruptedException {
        while (values.size() == maxSize) {
            System.out.println("producer is waiting");
            wait();
        }
        values.add((random.nextInt() % 50) + 50);
        System.out.println("produced: " + values.get(values.size() - 1) + " -> size: " + values.size());
        notify();
    }

    public synchronized void consume() throws InterruptedException {
        while (values.size() == 0) {
            System.out.println("consumer is waiting");
            wait();
        }
        System.out.println("consumed: " + values.get(0) + " -> size: " + values.size());
        values.remove(0);
        notify();
    }
}
