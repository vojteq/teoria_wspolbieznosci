package lab7.active_object;

import lab7.active_object.method_requests.ConsumeRequest;
import lab7.active_object.method_requests.ProduceRequest;
import static lab7.active_object.ColorUtil.print;

public class Scheduler {
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

    public void run() {
        int emptyQueues = 0;
        while (true) {
            if (activationQueue.produceRequestQueueNotEmpty()) {
                boolean a = activationQueue.firstProduceRequestCanBeExecuted();
                if (a) {
//                if (activationQueue.firstProduceRequestCanBeExecuted()) {
                    ProduceRequest produceRequest = activationQueue.dequeueProduceRequest();
                    print("executing produce request", Color.GREEN);
                    produceRequest.execute();
                }
            }
            else {
                emptyQueues++;
            }

            if (activationQueue.consumeRequestQueueNotEmpty()) {
                boolean a = activationQueue.firstConsumeRequestCanBeExecuted();
                if (a) {
//                if (activationQueue.firstConsumeRequestCanBeExecuted()) {
                    ConsumeRequest consumeRequest = activationQueue.dequeueConsumeRequest();
                    print("executing consume request", Color.BLUE);
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
