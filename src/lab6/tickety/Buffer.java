package lab6.tickety;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Buffer {
    private ConcurrentHashMap<Integer, Integer> values;
    private final int size;
    private final Random random;

    public Buffer(int size) {
        values = new ConcurrentHashMap<>();
        this.size = size;
        random = new Random();
    }

    public void produce(ProducerTicket ticket) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int producedValue = random.nextInt() % 50 + 50;
        values.put(ticket.getId(), producedValue);
        System.out.println("produced: " + producedValue);
    }

    public void consume(ConsumerTicket ticket) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("consumed: " + values.remove(ticket.getId()));
    }


    public boolean hasSpace() {
        return values.size() + 1 <= size;
    }

    public boolean hasElements() {
        return !values.isEmpty();
    }
}
