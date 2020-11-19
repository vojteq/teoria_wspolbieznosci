package lab6.tickety;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Buffer {
    private ConcurrentHashMap<Integer, Integer> values;
//    private final int SIZE;

    public Buffer(int size) {
        values = new ConcurrentHashMap<>();
    }

    public void produce(Ticket ticket, int value) {
//        this.longOperation();
        if (values.containsKey(ticket.getId())) {
            throw new RuntimeException("There already is product with tickedID=" + ticket.getId());
        }
        values.put(ticket.getId(), value);
        System.out.println("produced: " + value);
    }

    public void consume(Ticket ticket) {
//        this.longOperation();
        if (!values.containsKey(ticket.getId())) {
            throw new RuntimeException("There is no product with ticketID=" + ticket.getId());
        }
        System.out.println("consumed: " + values.remove(ticket.getId()));
    }

    private void longOperation() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
