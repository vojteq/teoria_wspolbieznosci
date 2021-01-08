package lab11_csp.active_object_time_mod.method_requests;

import lab11_csp.active_object_time_mod.Buffer;
import lab11_csp.active_object_time_mod.FutureIntegerList;

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
