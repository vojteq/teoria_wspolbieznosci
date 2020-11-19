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
        producerTickets = new LinkedList<>();
        consumerTickets = new LinkedList<>();
        MAX_SIZE = maxSize;
        for (int i = 0; i < MAX_SIZE; i++) {
            producerTickets.add(new Ticket(i));
        }
        lock = new ReentrantLock();
        producersCond = lock.newCondition();
        consumersCond = lock.newCondition();
    }

    public Ticket getProducerTicket() {
        Ticket ticket = null;
        lock.lock();
        try {
            while (producerTickets.isEmpty()) {
                System.out.println("producer " + Thread.currentThread().getId() + " is waiting");
                producersCond.await();
            }
            ticket = producerTickets.remove();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return ticket;
    }

    public Ticket getConsumerTicket() {
        Ticket ticket = null;
        lock.lock();
        try {
            while (consumerTickets.isEmpty()) {
                System.out.println("consumer " + Thread.currentThread().getId() + " is waiting");
                consumersCond.await();
            }
            ticket = consumerTickets.remove();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return ticket;
    }

    public void returnProducerTicket(Ticket ticket) {
        lock.lock();
        try {
            this.countThreads();
            consumerTickets.add(ticket);
            consumersCond.signal();
        } finally {
            lock.unlock();
        }
    }

    public void returnConsumerTicket(Ticket ticket) {
        lock.lock();
        try {
            this.countThreads();
            producerTickets.add(ticket);
            producersCond.signal();
        } finally {
            lock.unlock();
        }
    }

    private void countThreads() {
        System.out.println("threads in buffer: " + (MAX_SIZE - producerTickets.size() - consumerTickets.size()));
    }
}
