package lab5;

import javax.lang.model.type.NullType;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    // TODO: jaka struktura do values zeby sie dalo sprawdzic jej rozmiar ale zeby ZAPIS ODCZYT USUNIECIE byly O(1)
    // TODO: wyjebac tablice ...taken?
    private int[] values;
    private LinkedList<ProducerTicket> producerTickets;
    private LinkedList<ConsumerTicket> consumerTickets;
    private ProducerTicket[] producerTicketsTaken;
    private ConsumerTicket[] consumerTicketsTaken;
    private int maxSize;
    private Random random;
    private ReentrantLock producerTicketLock;
    private ReentrantLock consumerTicketLock;
    private Condition producersCond;
    private Condition consumersCond;

    public Monitor(int maxSize) {
        values = new int[maxSize];
        producerTickets = new LinkedList<>();
        consumerTickets = new LinkedList<>();
        producerTicketsTaken = new ProducerTicket[maxSize];
        consumerTicketsTaken = new ConsumerTicket[maxSize];
        for (int i = 0; i < maxSize; i++) {
            values[i] = -1;
            producerTickets.add(new ProducerTicket(i));
//            consumerTickets.add(new ConsumerTicket(i));
            producerTicketsTaken[i] = null;
            consumerTicketsTaken[i] = null;
        }
        random = new Random();
        this.maxSize = maxSize;
        producerTicketLock = new ReentrantLock();
        consumerTicketLock = new ReentrantLock();
        producersCond = producerTicketLock.newCondition();
        consumersCond = consumerTicketLock.newCondition();
    }

    public ProducerTicket getProducerTicket() {
        ProducerTicket ticket = null;
        producerTicketLock.lock();
        try {
            while (producerTickets.size() == 0) {
                System.out.println("producer is waiting");
                producersCond.await();
            }
            ticket = producerTickets.get(0);
            producerTickets.remove(0);
            producerTicketsTaken[ticket.getId()] = ticket;
            producersCond.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            producerTicketLock.unlock();
        }
        return ticket;
    }

    public ConsumerTicket getConsumerTicket() {
        ConsumerTicket ticket = null;
        consumerTicketLock.lock();
        try {
            while (consumerTickets.size() == 0) {
                System.out.println("producer is waiting");
                consumersCond.await();
            }
            ticket = consumerTickets.get(0);
            producerTickets.remove(0);
            consumerTicketsTaken[ticket.getId()] = ticket;
            consumersCond.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            consumerTicketLock.unlock();
        }
        return ticket;
    }

    public void produce(ProducerTicket ticket) {
        if (ticket.equals(producerTicketsTaken[ticket.getId()])) {
            values[ticket.getId()] = (random.nextInt() % 50) + 50;
            System.out.println("produced: " + values[ticket.getId()]);
            producerTickets.add(ticket);
        }
        else {
            System.out.println("producer ticket invalid");
        }
    }

    public void consume(ConsumerTicket ticket) {
        if (ticket.equals(consumerTicketsTaken[ticket.getId()])) {
            System.out.println("consumed: " + values[ticket.getId()]);
            values[ticket.getId()] = -1;
            consumerTickets.add(ticket);
            consumerTicketsTaken[ticket.getId()] = null;
        }
        else {
            System.out.println("consumer ticket invalid");
        }
    }
}
