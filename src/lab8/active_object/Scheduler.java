package lab8.active_object;

import lab8.active_object.method_requests.ConsumeRequest;
import lab8.active_object.method_requests.ProduceRequest;

public class Scheduler implements Runnable{
    private final ActivationQueue activationQueue;

    public Scheduler() {
        this.activationQueue = new ActivationQueue();
    }

    public void addProduceRequest(ProduceRequest produceRequest) {
        activationQueue.enqueueProduceRequest(produceRequest);
    }

    public void addConsumeRequest(ConsumeRequest consumeRequest) {
        activationQueue.enqueueConsumeRequest(consumeRequest);
    }

    public boolean needProducts() {
        return activationQueue.consumeRequestQueueNotEmpty();
    }

    @Override
    public void run() {
        int emptyQueues = 0;
        while (true) {
            if (activationQueue.produceRequestQueueNotEmpty()) {
                if (activationQueue.firstProduceRequestCanBeExecuted()) {
                    ProduceRequest produceRequest = activationQueue.dequeueProduceRequest();
                    produceRequest.execute();
                }
            }
            else {
                emptyQueues++;
            }

            if (activationQueue.consumeRequestQueueNotEmpty()) {
                if (activationQueue.firstConsumeRequestCanBeExecuted()) {
                    ConsumeRequest consumeRequest = activationQueue.dequeueConsumeRequest();
                    consumeRequest.execute();
                }
            }
            else {
                emptyQueues++;
            }
            if (emptyQueues == 2) {
                activationQueue.bothQueuesEmpty();
            }
            emptyQueues = 0;
        }
    }
}
