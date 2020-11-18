package lab6.x;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

// Producenci i konsumenci z asynchronicznym wstawianiem i usuwaniem do bufora (4.4.4)

class Buffer {

    public final int MAX_SIZE;
//    private HashMap<Integer, Integer> buffer = new HashMap<Integer, Integer>();
    private ConcurrentHashMap<Integer, Integer> buffer = new ConcurrentHashMap<Integer, Integer>();

    Buffer(int size){

        MAX_SIZE = size;
    }

    void veryLongProduce(int ticket, int newValue) throws InterruptedException {
        Random rnd = new Random();
//        sleep(100+rnd.nextInt(200));

//        if(this.buffer[ticket] != -1){
        if(this.buffer.containsKey(ticket)){
            throw new RuntimeException("W buforze "+ticket+" jest już produkt!");
        }
        int preSize = this.buffer.size();
        this.buffer.put(ticket, newValue);

//        this.buffer[ticket] = newValue;
    }

    void veryLongConsume(int ticket) throws InterruptedException {
        Random rnd = new Random();
//        sleep(100+rnd.nextInt(200));
//        if(this.buffer[ticket] == -1){
        if(!this.buffer.containsKey(ticket)){
            throw new RuntimeException("W buforze "+ticket+" nie ma produktu!");
        }
        this.buffer.remove(ticket);
//        this.buffer[ticket] = -1;
    }
}

class ProdCons {
    public final int MAX_SIZE;
//    private Integer[] buffer;
    private Queue<Integer> prodTickets = new LinkedList<Integer>();
    private Queue<Integer> consTickets = new LinkedList<Integer>();

    private Lock lock = new ReentrantLock();
    private Condition producerCond;
    private Condition consumerCond;
    int producers = 0;
    int consumers = 0;

    public ProdCons(int maxSize){
        this.MAX_SIZE = maxSize;
//        this.buffer = new Integer[maxSize];
//        Arrays.fill(buffer, -1);
        for(int i=0; i < this.MAX_SIZE; i++){
            this.prodTickets.add(i);
        }
        this.consumerCond = lock.newCondition();
        this.producerCond = lock.newCondition();
    }

    Integer getProdTicket(){
        return this.prodTickets.remove();
    }

    void pushProdTicket(Integer i){
        this.prodTickets.add(i);
    }

    Integer getConsTicket(){
        return this.consTickets.remove();
    }

    void pushConsTicket(Integer i){
        this.consTickets.add(i);
    }


    int prodStart(int id) throws InterruptedException {
        int ticket;
        lock.lock();
        try{
            while (prodTickets.isEmpty()){
                producerCond.await();
            }
            ticket = getProdTicket();
            producers++;
//            System.out.println("Producer "+ id + " got ticket " + ticket);
        }finally {
            lock.unlock();
        }
        return ticket;
    }

    void prodEnd(int ticket, int id) {
        lock.lock();
        try{
            this.countProcessesInBuffer();
            producers--;
            pushConsTicket(ticket);
            consumerCond.signal();
//            System.out.println("Producer "+ id + " returned ticket " + ticket);
        }finally {
            lock.unlock();
        }
    }

    int consumeStart(int id) throws InterruptedException {
        int ticket;
        lock.lock();
        try {
            while (consTickets.isEmpty()) {
                consumerCond.await();
            }
            ticket = getConsTicket();
            consumers++;
//            System.out.println("Consumer "+ id + " got ticket " + ticket);
        } finally {
            lock.unlock();
        }
        return ticket;
    }

    void consumeEnd (int ticket, int id) {
        lock.lock();
        try{
            this.countProcessesInBuffer();
            pushProdTicket(ticket);
//            System.out.println("Consumer "+ id + " returned ticket " + ticket);
            consumers--;
            producerCond.signal();
        } finally {
            lock.unlock();
        }
    }



    void countProcessesInBuffer() {
//        System.out.println("Procesy w buforze: "+ (this.consumers+this.producers));

//        System.out.println("Procesy w buforze: "+ ((this.MAX_SIZE-this.consTickets.size()-this.prodTickets.size()))
//        + "\tKonsumenci: " + this.consumers +"\tProducenci: " + this.producers);

        //Bufor: 10
        // tickety prod: 4
        // tickety kons: 3
        // Operacje wykonuje 10-4-3 procesów
    }



}


class Producer implements Runnable {

    private final int id;
    private final ProdCons pc;
    private final Buffer buff;

    public Producer(int id, ProdCons pc, Buffer buff){
        this.id = id;
        this.pc = pc;
        this.buff = buff;
    }


    public void run() {
        Random rand = new Random();
        while (true) {
            int newValue = rand.nextInt(20);
            try {
                int ticket = pc.prodStart(id);
                buff.veryLongProduce(ticket, newValue);
//                System.out.println("Producer "+ id + " produced to " + ticket);
                pc.prodEnd(ticket, id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}


class Consumer implements Runnable {

    private int id;
    private ProdCons pc;
    private final Buffer buff;

    public Consumer(int id, ProdCons pc, Buffer buff){
        this.id = id;
        this.pc = pc;
        this.buff = buff;
    }


    public void run() {
        long startTime = System.nanoTime();
        for(int i=0; ; i++){
//        while (true) {
            try {
                int ticket = pc.consumeStart(id);
                buff.veryLongConsume(ticket);
//                System.out.println("Consumer "+ id + " consumed from " + ticket);
                pc.consumeEnd(ticket, id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i ==1000 ){
//                System.out.println("ID "+this.id+ ": "+(System.nanoTime()-startTime)/1000000.0);
            }
        }
    }
}


public class Main {
    public static void main(String[] args) throws InterruptedException{

        int CONSUMERS = 20;
        int PRODUCERS = 20;
        int SIZE = 20;

        Thread[] consumer = new Thread[CONSUMERS];
        Thread[] producer = new Thread[PRODUCERS];
        ProdCons pc = new ProdCons(SIZE);
        Buffer buff = new Buffer(SIZE);


        for(int i=0; i<CONSUMERS; i++){
            consumer[i] = new Thread(new Consumer(i+1, pc, buff));
        }
        for(int i=0; i<PRODUCERS; i++){
            producer[i] = new Thread(new Producer(i+1, pc, buff));
        }

        for(int i=0; i<CONSUMERS; i++){
            consumer[i].start();
        }
        for(int i=0; i<PRODUCERS; i++){
            producer[i].start();
        }
    }
}
