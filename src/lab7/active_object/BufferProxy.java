package lab7.active_object;

import lab7.active_object.method_requests.ConsumeRequest;
import lab7.active_object.method_requests.ProduceRequest;

public class BufferProxy {
    private final Scheduler scheduler;
    private final Buffer buffer;

    public BufferProxy(int bufferSize, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.buffer = new Buffer(bufferSize);
    }

    public FutureIntegerList produceFuture(int n) {
        FutureIntegerList future = new FutureIntegerList();
        ProduceRequest produceRequest = new ProduceRequest(n, future, buffer);
        scheduler.addProduceRequest(produceRequest);
        return future;
    }

    public FutureIntegerList consumeFuture(int n) {
        FutureIntegerList future = new FutureIntegerList();
        ConsumeRequest consumeRequest = new ConsumeRequest(n, future, buffer);
        scheduler.addConsumeRequest(consumeRequest);
        return future;
    }
}
