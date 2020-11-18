package lab6.tickety;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Buffer buffer;
    private final LinkedList<ProducerTicket> producerTickets;
    private final LinkedList<ConsumerTicket> consumerTickets;
    private final LinkedList<ConsumerTicket> consumerTicketsGiven;
    private final int maxSize;
    private final ReentrantLock lock;
    private final Condition producersCond;
    private final Condition consumersCond;


    public Monitor(int maxSize, Buffer buffer) {
        producerTickets = new LinkedList<>();
        consumerTickets = new LinkedList<>();
        consumerTicketsGiven = new LinkedList<>();
        this.buffer = buffer;
        this.maxSize = maxSize;
        for (int i = 0; i < maxSize; i++) {
            producerTickets.add(new ProducerTicket(i));
        }
        lock = new ReentrantLock();
        producersCond = lock.newCondition();
        consumersCond = lock.newCondition();
    }

    public ProducerTicket getProducerTicket() {
        ProducerTicket ticket = null;
        this.lock.lock();
        try {
            while (producerTickets.size() == 0 || !buffer.hasSpace()) {
                System.out.println("producer " + Thread.currentThread().getId() + " is waiting");
                producersCond.await();
            }
            ticket = producerTickets.remove(0);
            producersCond.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            this.lock.unlock();
        }
        return ticket;
    }

    public ConsumerTicket getConsumerTicket() {
        ConsumerTicket ticket = null;
        this.lock.lock();
        try {
            while (consumerTickets.size() == 0 || !buffer.hasElements()) {
                System.out.println("consumer " + Thread.currentThread().getId() + " is waiting");
                consumersCond.await();
            }
            ticket = consumerTickets.remove(0);
            consumerTicketsGiven.add(ticket);
            consumersCond.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            this.lock.unlock();
        }
        return ticket;
    }

    public void produce(ProducerTicket ticket) {
        buffer.produce(ticket);
        consumerTickets.add(new ConsumerTicket(ticket.getId()));
        producerTickets.add(ticket);

        lock.lock();
        int threads = maxSize - producerTickets.size() + consumerTicketsGiven.size();
        System.out.println("(P)threads in buffer: " + threads);
        lock.unlock();
    }

    public void consume(ConsumerTicket ticket) {

        lock.lock();
        int threads = maxSize - producerTickets.size() + consumerTicketsGiven.size();
        consumerTicketsGiven.remove(ticket);
        System.out.println("(C)threads in buffer: " + threads);
        lock.unlock();
    }
}