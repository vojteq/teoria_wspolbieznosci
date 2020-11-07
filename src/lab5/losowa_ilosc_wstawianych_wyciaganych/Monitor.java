package lab5.losowa_ilosc_wstawianych_wyciaganych;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private ArrayList<Integer> values;
    private int maxSize;
    private Random random;
    private ReentrantLock lock;
    private Condition restProducers;
    private Condition firstProducer;
    private Condition restConsumers;
    private Condition firstConsumer;
    private boolean isFirstProducer = false;
    private boolean isFirstConsumer = false;

    public Monitor(int maxSize) {
        values = new ArrayList<>();
        random = new Random();
        this.maxSize = maxSize;
        lock = new ReentrantLock();
        restProducers = lock.newCondition();
        firstProducer = lock.newCondition();
        restConsumers = lock.newCondition();
        firstConsumer = lock.newCondition();
    }

    public void produce(List<Integer> data) throws InterruptedException {
        lock.lock();
        try {
//            while (lock.hasWaiters(firstProducer)) {
            while (isFirstProducer) {
                restProducers.await();
            }
            while (!hasEnoughSpace(data.size())) {
                System.out.println("producer is waiting");
                isFirstProducer = true;
                firstProducer.await();
            }
            isFirstProducer = false;
            values.addAll(data);
            System.out.println("(producer " + Thread.currentThread().getId() + " ) produced: " + values.get(values.size() - 1) + " -> size: " + values.size());
            restProducers.signal();
            firstConsumer.signal();
        }
        finally {
            lock.unlock();
        }
    }

    private boolean hasEnoughSpace(int size) {
        return size + values.size() > maxSize;
    }

    public List<Integer> consume(int dataSize) throws InterruptedException {
        List<Integer> consumed = new ArrayList<>();
        lock.lock();
        try {
//            while (lock.hasWaiters(firstConsumer)) {
            while (isFirstConsumer) {
                restConsumers.await();
            }
            while (!hasEnoughData(dataSize)) {
                System.out.println("consumer is waiting");
                isFirstConsumer = true;
                restConsumers.await();
            }
            isFirstConsumer = false;
            for (int i = 0; i < dataSize; i++) {
                consumed.add(values.remove(0));
            }
            System.out.println("(consumer " + Thread.currentThread().getId() + " ) consumed: " + dataSize + " elements -> size: " + values.size());
            values.remove(0);
            restConsumers.signal();
            firstProducer.signal();
        }
        finally {
            lock.unlock();
        }
        return consumed;
    }

    private boolean hasEnoughData(int dataSize) {
        return values.size() >= dataSize;
    }
}
