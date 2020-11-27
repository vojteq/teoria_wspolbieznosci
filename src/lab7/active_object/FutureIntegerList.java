package lab7.active_object;

import java.util.LinkedList;
import java.util.concurrent.*;

public class FutureIntegerList implements Future<LinkedList<Integer>> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private LinkedList<Integer> result;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    @Override
    public LinkedList<Integer> get() throws InterruptedException, ExecutionException {
        latch.await();
        return result;
    }

    @Override
    public LinkedList<Integer> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return result;
        }
        else {
            throw new TimeoutException();
        }
    }

    public void put(LinkedList<Integer> values) {
        result = values;
    }
}
