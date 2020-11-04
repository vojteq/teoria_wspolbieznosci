package lab5;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    // TODO tablica values i lista indeksow z pod ktorych mozna wyciagac / pod ktore mozna wkladac (2 listy)
    // klucz -> id producenta
    // wartpsc -> wyprodukowana liczba
    private final int[] values;
    private final LinkedList<ProducerTicket> producerTickets;

    // klucz -> id producenta ktory wyprodukowal a wiec stworzyl ConsumerTicket
    // wartosc ->
    private final LinkedList<ConsumerTicket> consumerTickets;

    // id producenta ktory wyprodukowal, po zjedzeniu przez konsumenta usuwamy jedna wartosc
    private final int maxSize;
    private final Random random;
    private final ReentrantLock producerTicketLock;
    private final ReentrantLock consumerTicketLock;
    private final Condition producersCond;
    private final Condition consumersCond;


    public Monitor(int maxSize) {
        values = new int[maxSize];
        producerTickets = new LinkedList<>();
        consumerTickets = new LinkedList<>();

        for (int i = 0; i < maxSize; i++) {
            producerTickets.add(new ProducerTicket(i));
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
            producerTicketLock.unlock();
        }
        return ticket;
    }

    public ConsumerTicket getConsumerTicket() {
        ConsumerTicket ticket = null;
        consumerTicketLock.lock();
        try {
            while (consumerTickets.size() == 0) {
                System.out.println("consumer " + Thread.currentThread().getId() + " is waiting");
                consumersCond.await();
            }
            ticket = consumerTickets.remove(0);
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
        int producedValue = random.nextInt() % 50 + 50;
        values[ticket.getId()] = producedValue;
        consumerTickets.add(new ConsumerTicket(ticket.getId()));
        System.out.println("produced: " + producedValue);
        producerTickets.add(ticket);
    }

    public void consume(ConsumerTicket ticket) {
        System.out.println("consumed: " + values[ticket.getId()]);
    }
}




//package lab5;
//
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.Random;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class Monitor {
//    // TODO tablica values i lista indeksow z pod ktorych mozna wyciagac / pod ktore mozna wkladac (2 listy)
//    // klucz -> id producenta
//    // wartpsc -> wyprodukowana liczba
//    private final LinkedHashMap<Integer, Integer> values;
//    private final LinkedList<ProducerTicket> producerTickets;
//
//    // klucz -> id producenta ktory wyprodukowal a wiec stworzyl ConsumerTicket
//    // wartosc ->
//    private final LinkedHashMap<Integer, ConsumerTicket> consumerTickets;
//
//    // id producenta ktory wyprodukowal, po zjedzeniu przez konsumenta usuwamy jedna wartosc
//    private final LinkedList<Integer> producedBy;
//    private final int maxSize;
//    private final Random random;
//    private final ReentrantLock producerTicketLock;
//    private final ReentrantLock consumerTicketLock;
//    private final Condition producersCond;
//    private final Condition consumersCond;
//
//
//    public Monitor(int maxSize) {
//        values = new LinkedHashMap<>();
//        producerTickets = new LinkedList<>();
//        consumerTickets = new LinkedHashMap<>();
//        producedBy = new LinkedList<>();
//        for (int i = 0; i < maxSize; i++) {
//            producerTickets.add(new ProducerTicket(i));
//        }
//        random = new Random();
//        this.maxSize = maxSize;
//        producerTicketLock = new ReentrantLock();
//        consumerTicketLock = new ReentrantLock();
//        producersCond = producerTicketLock.newCondition();
//        consumersCond = consumerTicketLock.newCondition();
//    }
//
//    public ProducerTicket getProducerTicket() {
//        ProducerTicket ticket = null;
//        producerTicketLock.lock();
//        try {
//            while (values.size() == maxSize || producerTickets.size() == 0) {
//                System.out.println("producer " + Thread.currentThread().getId() + " is waiting");
//                producersCond.await();
//            }
//            ticket = producerTickets.get(0);
//            producerTickets.remove(0);
//            producersCond.signal();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        finally {
//            producerTicketLock.unlock();
//        }
//        return ticket;
//    }
//
//    public ConsumerTicket getConsumerTicket() {
//        ConsumerTicket ticket = null;
//        consumerTicketLock.lock();
//        try {
//            while (consumerTickets.size() == 0) {
//                System.out.println("consumer " + Thread.currentThread().getId() + " is waiting");
//                consumersCond.await();
//            }
//            ticket = consumerTickets.get(0);
//            consumerTickets.remove(producedBy.remove(0));
//            consumersCond.signal();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        finally {
//            consumerTicketLock.unlock();
//        }
//        return ticket;
//    }
//
//    public void produce(ProducerTicket ticket) {
//        int producedValue = random.nextInt() % 50 + 50;
//        values.put(ticket.getId(), producedValue);
//        producedBy.add(ticket.getId());
//        System.out.println("produced: " + producedValue);
//        producerTickets.add(ticket);
//        consumerTickets.put(ticket.getId(), new ConsumerTicket(ticket.getId()));
//    }
//
//    public void consume(ConsumerTicket ticket) {
//        System.out.println("consumed: " + values.remove(ticket.getId()));
//    }
//}