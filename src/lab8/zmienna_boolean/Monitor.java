package lab8.zmienna_boolean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final ArrayList<Integer> values;
    private final int MAX_SIZE;
    private final ReentrantLock lock;
    private final Condition restProducersCond;
    private final Condition firstProducerCond;
    private final Condition restConsumersCond;
    private final Condition firstConsumerCond;
    private boolean isFirstProducer = false;
    private boolean isFirstConsumer = false;
    private final int PRODUCTON_DELAY;
    private final int CONSUMPTON_DELAY;
    private boolean needProducts = false;

    public Monitor(int maxSize, int productionDelay, int consumptionDelay) {
        values = new ArrayList<>();
        this.MAX_SIZE = maxSize;
        lock = new ReentrantLock();
        restProducersCond = lock.newCondition();
        firstProducerCond = lock.newCondition();
        restConsumersCond = lock.newCondition();
        firstConsumerCond = lock.newCondition();
        this.PRODUCTON_DELAY = productionDelay;
        this.CONSUMPTON_DELAY = consumptionDelay;
    }

    public void produce(int itemsToBeProduced) throws InterruptedException {
        lock.lock();
        try {
            while (isFirstProducer) {
                restProducersCond.await();
            }
            while (!hasEnoughSpace(itemsToBeProduced)) {
                isFirstProducer = true;
                firstProducerCond.await();
            }
            isFirstProducer = false;
            for (int i = 0; i < itemsToBeProduced; i++) {
                values.add(i);
            }
            Thread.sleep(PRODUCTON_DELAY);
            restProducersCond.signal();
            firstConsumerCond.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public List<Integer> consume(int itemsToBeConsumed) throws InterruptedException {
        List<Integer> consumed = new ArrayList<>();
        lock.lock();
        try {
            while (isFirstConsumer) {
                restConsumersCond.await();
            }
            while (!hasEnoughData(itemsToBeConsumed)) {
                needProducts = true;
                isFirstConsumer = true;
                firstConsumerCond.await();
            }
            isFirstConsumer = false;
            for (int i = 0; i < itemsToBeConsumed; i++) {
                consumed.add(values.remove(0));
            }
            Thread.sleep(CONSUMPTON_DELAY);
            restConsumersCond.signal();
            firstProducerCond.signal();
        }
        finally {
            lock.unlock();
        }
        return consumed;
    }

    private boolean hasEnoughSpace(int size) {
        return size + values.size() <= MAX_SIZE;
    }

    private boolean hasEnoughData(int dataSize) {
        return values.size() >= dataSize;
    }

    public boolean needProducts() {
        lock.lock();
        try {
            return this.needProducts;
        } finally {
            lock.unlock();
        }
    }

    public void dontNeedProducts() {
        lock.lock();
        try {
            needProducts = false;
        } finally {
            lock.unlock();
        }
    }
}
