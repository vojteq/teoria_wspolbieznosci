package lab7.active_object;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        int bufferSize = 30;
        int producers = 2;
        int consumers = 3;
        Scheduler scheduler = new Scheduler();
        BufferProxy bufferProxy = new BufferProxy(bufferSize, scheduler);
        LinkedList<Thread> producerThreads = new LinkedList<>();
        LinkedList<Thread> consumerThreads = new LinkedList<>();

        createThreads(producers, producerThreads, consumers, consumerThreads, bufferProxy);
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();
        for (Thread thread : producerThreads) {
            thread.start();
        }
        for (Thread thread : consumerThreads) {
            thread.start();
        }
    }

    private static void createThreads(int producers, LinkedList<Thread> producerThreads,
                                      int consumers, LinkedList<Thread> consumerThreads, BufferProxy bufferProxy) {
        for (int i = 0; i  < producers; i++) {
            producerThreads.add(new Thread(new Producer(bufferProxy)));
        }
        for (int i = 0; i  < consumers; i++) {
            consumerThreads.add(new Thread(new Consumer(bufferProxy)));
        }
    }
}
