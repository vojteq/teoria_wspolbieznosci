package lab8.active_object;

import java.util.LinkedList;

public class Buffer {
    private final int MAX_SIZE;
    private final LinkedList<Integer> values = new LinkedList<>();
    private final int PRODUCTION_DELAY;
    private final int CONSUMPTION_DELAY;

    public Buffer(int maxSize, int productionDelay, int consumptionDelay) {
        this.MAX_SIZE = maxSize;
        this.PRODUCTION_DELAY = productionDelay;
        this.CONSUMPTION_DELAY = consumptionDelay;
    }

    public void produce(LinkedList<Integer> list) {
        values.addAll(list);
        try {
            Thread.sleep(PRODUCTION_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Integer> consume(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = n; i > 0; i--) {
            list.add(values.remove());
        }
        try {
            Thread.sleep(CONSUMPTION_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
