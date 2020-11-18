package lab6.tickety;

public class Consumer implements Runnable {
    private Monitor monitor;

    public Consumer(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        ConsumerTicket ticket = null;
        while (true) {
            ticket = monitor.getConsumerTicket();
            monitor.consume(ticket);
            ticket = null;
        }
    }
}
