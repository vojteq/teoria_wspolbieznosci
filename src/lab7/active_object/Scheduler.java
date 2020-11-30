package lab7.active_object;

import lab7.active_object.method_requests.ConsumeRequest;
import lab7.active_object.method_requests.ProduceRequest;
import static lab7.active_object.ColorUtil.print;

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

    @Override
    public void run() {
        int emptyQueues = 0;
        while (true) {
            if (activationQueue.produceRequestQueueNotEmpty()) {
                if (activationQueue.firstProduceRequestCanBeExecuted()) {
                    ProduceRequest produceRequest = activationQueue.dequeueProduceRequest();
                    print("executing produce request", Color.YELLOW);
                    produceRequest.execute();
                }
            }
            else {
                emptyQueues++;
            }

            if (activationQueue.consumeRequestQueueNotEmpty()) {
                if (activationQueue.firstConsumeRequestCanBeExecuted()) {
                    ConsumeRequest consumeRequest = activationQueue.dequeueConsumeRequest();
                    print("executing consume request", Color.MAGENTA);
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
