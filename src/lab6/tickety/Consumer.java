package lab6.tickety;

public class Consumer implements Runnable {
    private final Monitor monitor;
    private final Buffer buffer;

    public Consumer(Monitor monitor, Buffer buffer) {
        this.monitor = monitor;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        Ticket ticket = null;
        while (true) {
            ticket = monitor.getConsumerTicket();
//            monitor.consume(ticket);
            buffer.consume(ticket);
            monitor.returnConsumerTicket(ticket);
            ticket = null;
        }
    }
}
