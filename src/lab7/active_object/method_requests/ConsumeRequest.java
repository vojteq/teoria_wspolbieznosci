package lab7.active_object.method_requests;

import lab7.active_object.Buffer;
import lab7.active_object.FutureIntegerList;

public class ConsumeRequest implements MethodRequest {
    private final int n;
    private final FutureIntegerList future;
    private final Buffer buffer;

    public ConsumeRequest(int n, FutureIntegerList future, Buffer buffer) {
        this.n = n;
        this.future = future;
        this.buffer = buffer;
    }

    @Override
    public boolean guard() {
        return buffer.canConsume(n);
    }

    @Override
    public void execute() {
        future.put(buffer.consume(n));
    }
}
