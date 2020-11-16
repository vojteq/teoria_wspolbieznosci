package lab5.zadanie.zmienna_boolean;

import java.util.ArrayList;
import java.util.List;
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

    public Monitor(int maxSize) {
        values = new ArrayList<>();
        this.MAX_SIZE = maxSize;
        lock = new ReentrantLock();
        restProducersCond = lock.newCondition();
        firstProducerCond = lock.newCondition();
        restConsumersCond = lock.newCondition();
        firstConsumerCond = lock.newCondition();
    }

    public void produce(Producer producer, List<Integer> data) throws InterruptedException {
        lock.lock();
        try {
            while (isFirstProducer) {
                System.out.println("producer" + producer.getId() + " is waiting (not first)");
                restProducersCond.await();
            }
            while (!hasEnoughSpace(data.size())) {
                System.out.println("producer" + producer.getId() + " is waiting" +
                        " (not enough space (size:" + values.size() + ", max size:" + MAX_SIZE +  ", to insert:" + data.size() + ")");
                isFirstProducer = true;
                firstProducerCond.await();
            }
            isFirstProducer = false;
            values.addAll(data);
            System.out.println("(producer" + producer.getId() + " (" + producer.count() + ")" + ")" +
                    " produced(" + data.size() + "): " + data + " -> size: " + values.size());
            restProducersCond.signal();
            firstConsumerCond.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public List<Integer> consume(Consumer consumer, int dataSize) throws InterruptedException {
        List<Integer> consumed = new ArrayList<>();
        lock.lock();
        try {
            while (isFirstConsumer) {
                System.out.println("consumer" + consumer.getId() + " is waiting (not first)");
                restConsumersCond.await();
            }
            while (!hasEnoughData(dataSize)) {
                System.out.println("consumer" + consumer.getId() + " is waiting" +
                        " (not enough items (available: " + values.size() + ", requested: " + dataSize + ")");
                isFirstConsumer = true;
                firstConsumerCond.await();
            }
            isFirstConsumer = false;
            for (int i = 0; i < dataSize; i++) {
                consumed.add(values.remove(0));
            }
            System.out.println("(consumer" + consumer.getId() + " (" + consumer.count() + ")" + ")" +
                    " consumed(" + dataSize + "): " + consumed + " -> size: " + values.size());
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
}
