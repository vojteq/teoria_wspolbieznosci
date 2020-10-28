package lab4;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private ArrayList<Integer> values;
    private int maxSize;
    private Random random;
    private ReentrantLock lock;
    private Condition producersCond;
    private Condition consumersCond;

    public Monitor(int maxSize) {
        values = new ArrayList<>();
        random = new Random();
        this.maxSize = maxSize;
        lock = new ReentrantLock();
        producersCond = lock.newCondition();
        consumersCond = lock.newCondition();
    }

    public void produce() throws InterruptedException {
        lock.lock();
        try {
            while (values.size() == maxSize) {
                System.out.println("producer is waiting");
                producersCond.await();
            }
            values.add((random.nextInt() % 50) + 50);
            System.out.println("produced: " + values.get(values.size() - 1) + " -> size: " + values.size());
            consumersCond.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (values.size() == 0) {
                System.out.println("consumer is waiting");
                consumersCond.await();
            }
            System.out.println("consumed: " + values.get(0) + " -> size: " + values.size());
            values.remove(0);
            producersCond.signal();
        }
        finally {
            lock.unlock();
        }
    }
}
