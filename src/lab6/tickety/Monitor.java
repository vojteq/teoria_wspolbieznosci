package lab6.tickety;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Queue<Ticket> producerTickets;
    private final Queue<Ticket> consumerTickets;
    private final int MAX_SIZE;
    private final ReentrantLock lock;
    private final Condition producersCond;
    private final Condition consumersCond;


    public Monitor(int maxSize) {
        this.producerTickets = new LinkedList<>();
        this.consumerTickets = new LinkedList<>();
        this.MAX_SIZE = maxSize;
        for (int i = 0; i < MAX_SIZE; i++) {
            producerTickets.add(new Ticket(i));
        }
        this.lock = new ReentrantLock();
        this.producersCond = lock.newCondition();
        this.consumersCond = lock.newCondition();
    }

    public Ticket getProducerTicket() {
        Ticket ticket = null;
        this.lock.lock();
        try {
            while (this.producerTickets.isEmpty()) {
                System.out.println("producer " + Thread.currentThread().getId() + " is waiting");
                this.producersCond.await();
            }
            ticket = this.producerTickets.remove();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            this.lock.unlock();
        }
        return ticket;
    }

    public Ticket getConsumerTicket() {
        Ticket ticket = null;
        this.lock.lock();
        try {
            while (this.consumerTickets.isEmpty()) {
                System.out.println("consumer " + Thread.currentThread().getId() + " is waiting");
                this.consumersCond.await();
            }
            ticket = this.consumerTickets.remove();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            this.lock.unlock();
        }
        return ticket;
    }

    public void returnProducerTicket(Ticket ticket) {
        this.lock.lock();
        try {
            this.countThreads();
            this.consumerTickets.add(ticket);
            this.consumersCond.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void returnConsumerTicket(Ticket ticket) {
        this.lock.lock();
        try {
            this.countThreads();
            this.producerTickets.add(ticket);
            this.producersCond.signal();
        } finally {
            this.lock.unlock();
        }
    }

    private void countThreads() {
        System.out.println("threads in buffer: " + (this.MAX_SIZE - producerTickets.size() - consumerTickets.size()));
    }
}
