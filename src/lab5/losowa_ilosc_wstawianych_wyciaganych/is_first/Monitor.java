package lab5.losowa_ilosc_wstawianych_wyciaganych.is_first;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private ArrayList<Integer> values;
    private final int MAX_SIZE;
    private ReentrantLock lock;
    private Condition restProducersCond;
    private Condition firstProducerCond;
    private Condition restConsumersCond;
    private Condition firstConsumerCond;
    private boolean isFirstProducer = false;
    private boolean isFirstConsumer = false;

    public Monitor(int maxSize) {
        values = new ArrayList<>();
        this.MAX_SIZE = maxSize;
        lock = new ReentrantLock();
        restProducersCond = lock.newCondition();
        firstProducerCond = lock.newCondition();
        restConsumersCond = lock.newCondition();
        firstConsumerCond = lock.newCondition();
    }

    public void produce(int producerId, List<Integer> data) throws InterruptedException {
        lock.lock();
        try {
//            while (lock.hasWaiters(firstProducerCond)) {
            while (isFirstProducer) {
                restProducersCond.await();
            }
            while (!hasEnoughSpace(data.size())) {
                System.out.println("producer" + producerId + " is waiting");
                isFirstProducer = true;
                firstProducerCond.await();
            }
            isFirstProducer = false;
            values.addAll(data);
            System.out.println("(producer" + producerId + ") produced(" + data.size() + "): " + data + " -> size: " + values.size());
            restProducersCond.signal();
            firstConsumerCond.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public List<Integer> consume(int consumerId, int dataSize) throws InterruptedException {
        List<Integer> consumed = new ArrayList<>();
        lock.lock();
        try {
//            while (lock.hasWaiters(firstConsumerCond)) {
            while (isFirstConsumer) {
                restConsumersCond.await();
            }
            while (!hasEnoughData(dataSize)) {
                System.out.println("consumer" + consumerId + " is waiting");
                isFirstConsumer = true;
                firstConsumerCond.await();
            }
            isFirstConsumer = false;
            for (int i = 0; i < dataSize; i++) {
                consumed.add(values.remove(0));
            }
            System.out.println("(consumer" + consumerId + ") consumed(" + dataSize + "): " + consumed + " -> size: " + values.size());
            restConsumersCond.signal();
            firstProducerCond.signal();
        }
        finally {
            lock.unlock();
        }
        return consumed;
    }

    private boolean hasEnoughSpace(int size) {
        return size + values.size() < MAX_SIZE;
    }

    private boolean hasEnoughData(int dataSize) {
        return values.size() >= dataSize;
    }
}
