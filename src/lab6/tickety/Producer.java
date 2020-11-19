package lab6.tickety;

import java.util.Random;

public class Producer implements Runnable {
    private final Monitor monitor;
    private final Buffer buffer;

    public Producer(Monitor monitor, Buffer buffer) {
        this.monitor = monitor;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        Random random = new Random();
        Ticket ticket = null;
        while (true) {
            ticket = monitor.getProducerTicket();
//            monitor.produce(ticket);
            int producedValue = random.nextInt() % 50 + 50;
            buffer.produce(ticket, producedValue);
            monitor.returnProducerTicket(ticket);
            ticket = null;
        }
    }
}
