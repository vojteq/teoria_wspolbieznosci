package lab7.active_object;

import lab7.active_object.method_requests.ConsumeRequest;
import lab7.active_object.method_requests.ProduceRequest;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ActivationQueue {
    private final LinkedList<ProduceRequest> produceQueue = new LinkedList<>();
    private final LinkedList<ConsumeRequest> consumeQueue = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition bothQueuesEmptyQueue = lock.newCondition();

    public ActivationQueue() {
    }

    public void enqueueProduceRequet(ProduceRequest produceRequest) {
        lock.lock();
        try {
            produceQueue.add(produceRequest);
            bothQueuesEmptyQueue.signal();
        } finally {
            lock.unlock();
        }
    }

    public ProduceRequest dequeueProduceRequest() {
        ProduceRequest request = null;
        lock.lock();
        try {
            if (produceQueue.size() != 0)
                request = produceQueue.remove();
            return request;
        } finally {
            lock.unlock();
        }
    }

    public void enqueueConsumeRequet(ConsumeRequest consumeRequest) {
        lock.lock();
        try {
            consumeQueue.add(consumeRequest);
            bothQueuesEmptyQueue.signal();
        } finally {
            lock.unlock();
        }
    }

    public ConsumeRequest dequeueConsumeeRequest() {
        ConsumeRequest request = null;
        lock.lock();
        try {
            if (produceQueue.size() != 0)
                request = consumeQueue.remove();
            return request;
        } finally {
            lock.unlock();
        }
    }

    public boolean produceRequestQueueNotEmpty() {
        return !produceQueue.isEmpty();
    }

    public boolean consumeRequestQueueNotEmpty() {
        return !consumeQueue.isEmpty();
    }

    public boolean firstProduceRequestCanBeExecuted() {
        lock.lock();
        try {
            return produceQueue.getFirst().guard();
        } finally {
            lock.unlock();
        }
    }

    public boolean firstConsumeRequestCanBeExecuted() {
        lock.lock();
        try {
            return consumeQueue.getFirst().guard();
        } finally {
            lock.unlock();
        }
    }

    public void bothQueuesEmpty() {
        lock.lock();
        try {
            bothQueuesEmptyQueue.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
