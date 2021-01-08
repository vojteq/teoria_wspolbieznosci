package lab11_csp.active_object_time_mod;

import lab11_csp.active_object_time_mod.method_requests.ConsumeRequest;
import lab11_csp.active_object_time_mod.method_requests.ProduceRequest;

import java.util.Date;

public class Scheduler implements Runnable{
    private final ActivationQueue activationQueue;
    private final long MAX_TIME;

    public Scheduler(long maxTime) {
        this.activationQueue = new ActivationQueue();
        this.MAX_TIME = maxTime;
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
        long startTime = new Date().getTime();
        while (new Date().getTime() - startTime < this.MAX_TIME) {
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
            if (emptyQueues == 2 && new Date().getTime() - startTime < this.MAX_TIME) {
                activationQueue.bothQueuesEmpty();
            }
            emptyQueues = 0;
        }
    }
}
