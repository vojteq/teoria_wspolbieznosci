package lab7.active_object;

import java.util.LinkedList;

public class Buffer {
    private final int MAX_SIZE;
    private final LinkedList<Integer> values = new LinkedList<>();

    public Buffer(int maxSize) {
        this.MAX_SIZE = maxSize;
    }

    public void produce(LinkedList<Integer> list) {
        values.addAll(list);
    }

    public LinkedList<Integer> consume(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = n; i > 0; i--) {
            list.add(values.remove());
        }
        return list;
    }

    public boolean canProduce(int n) {
        return MAX_SIZE - values.size() >= n;
    }

    public boolean canConsume(int n) {
        return values.size() >= n;
    }
}
