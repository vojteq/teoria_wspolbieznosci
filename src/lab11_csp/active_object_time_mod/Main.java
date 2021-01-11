package lab11_csp.active_object_time_mod;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        int bufferSize = 4;
        int threads = 16;
        int operationDelay = 100;
        long maxTime = 100000;
        Scheduler scheduler = new Scheduler(maxTime);
        BufferProxy bufferProxy = new BufferProxy(bufferSize, scheduler);
        LinkedList<Thread> producerThreads = new LinkedList<>();
        LinkedList<Thread> consumerThreads = new LinkedList<>();

        createThreads(threads,
                producerThreads, consumerThreads,
                maxTime, operationDelay,
                bufferProxy);
        Thread schedulerThread = new Thread(scheduler);

        schedulerThread.start();
        for (Thread thread : producerThreads) {
            thread.start();
        }
        for (Thread thread : consumerThreads) {
            thread.start();
        }
        System.out.println("BUFFER: " + bufferSize);
    }

    private static void createThreads(int threads,
                                      LinkedList<Thread> producerThreads, LinkedList<Thread> consumerThreads,
                                      long maxTime, int operationDelay,
                                      BufferProxy bufferProxy) {
        for (int i = 0; i  < threads; i++) {
            producerThreads.add(new Thread(new Producer(i, bufferProxy,  maxTime, operationDelay)));
            consumerThreads.add(new Thread(new Consumer(i, bufferProxy,  maxTime, operationDelay)));
        }
    }
}
