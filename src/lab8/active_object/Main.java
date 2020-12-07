package lab8.active_object;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        int bufferSize = 20;
        int threads = 1;
        int operationsToDo = 1600;
        int operationDelay = 20;
        int additionalTask = 100;
        Scheduler scheduler = new Scheduler();
        BufferProxy bufferProxy = new BufferProxy(bufferSize, scheduler, operationDelay, operationDelay);
        LinkedList<Thread> producerThreads = new LinkedList<>();
        LinkedList<Thread> consumerThreads = new LinkedList<>();

        createThreads(threads,
                producerThreads, consumerThreads,
                operationsToDo, additionalTask,
                bufferProxy, bufferSize);
        Thread schedulerThread = new Thread(scheduler);

        schedulerThread.start();
        for (Thread thread : producerThreads) {
            thread.start();
        }
        for (Thread thread : consumerThreads) {
            thread.start();
        }

        // program nigdy sie nie skonczy bo scheduler dziala w nieskonczonosc,
        // ale zeby go wylaczyc musialbym dac mu zmienna (np running)
        // ale to znowu trzeba by synchronizowac bo dostep z zewnatrz... bez sensowna strata czasu a nie wazne w zadaniu
    }

    private static void createThreads(int threads,
                                      LinkedList<Thread> producerThreads, LinkedList<Thread> consumerThreads,
                                      int operationsToDo, int additionalTask,
                                      BufferProxy bufferProxy, int bufferSize) {
        for (int i = 0; i  < threads; i++) {
            producerThreads.add(new Thread(new Producer(i, bufferProxy, bufferSize / 2, operationsToDo, additionalTask)));
            consumerThreads.add(new Thread(new Consumer(i, bufferProxy, bufferSize / 2, operationsToDo, additionalTask)));
        }
    }
}
