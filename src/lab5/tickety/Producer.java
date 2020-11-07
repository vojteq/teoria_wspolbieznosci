package lab5.tickety;

public class Producer implements Runnable {
    private Monitor monitor;

    public Producer(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        ProducerTicket ticket = null;
        while (true) {
            ticket = monitor.getProducerTicket();
            monitor.produce(ticket);
            ticket = null;
        }
    }
}
