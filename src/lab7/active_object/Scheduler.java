package lab7.active_object;

import lab7.active_object.method_requests.ConsumeRequest;
import lab7.active_object.method_requests.ProduceRequest;

public class Scheduler {
    private final ActivationQueue activationQueue;

    public Scheduler() {
        this.activationQueue = new ActivationQueue();
    }

    public void addProduceRequest(ProduceRequest produceRequest) {
        activationQueue.enqueueProduceRequet(produceRequest);
    }

    public void addConsumeRequest(ConsumeRequest consumeRequest) {
        activationQueue.enqueueConsumeRequet(consumeRequest);
    }

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
                    ConsumeRequest consumeRequest = activationQueue.dequeueConsumeeRequest();
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
