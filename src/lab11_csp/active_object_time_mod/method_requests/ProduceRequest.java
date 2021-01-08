package lab11_csp.active_object_time_mod.method_requests;

import lab11_csp.active_object_time_mod.Buffer;
import lab11_csp.active_object_time_mod.FutureIntegerList;

import java.util.LinkedList;
import java.util.Random;

public class ProduceRequest implements MethodRequest {
    private final int n;
    private final FutureIntegerList future;
    private final Buffer buffer;

    public ProduceRequest(int n, FutureIntegerList future, Buffer buffer) {
        this.n = n;
        this.future = future;
        this.buffer = buffer;
    }

    @Override
    public boolean guard() {
        return buffer.canProduce(n);
    }

    @Override
    public void execute() {
        LinkedList<Integer> produced = new LinkedList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            produced.add(random.nextInt() % 10 + 10);
        }
        buffer.produce(produced);
        future.put(produced);
    }
}
