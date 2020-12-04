package lab8.active_object;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        int bufferSize = 20;
        int producers = 10;
        int consumers = 10;
        int productionsToDo = 100;
        int consumptionsToDo = 100;
        int productionDelay = 10;
        int consumptionDelay = 10;
        int producerAdditionalTaskTime = 10;
        int consumerAdditionalTaskTime = 10;
        Scheduler scheduler = new Scheduler();
        BufferProxy bufferProxy = new BufferProxy(bufferSize, scheduler, productionDelay, consumptionDelay);
        LinkedList<Thread> producerThreads = new LinkedList<>();
        LinkedList<Thread> consumerThreads = new LinkedList<>();

        createThreads(producers, consumers,
                producerThreads, consumerThreads,
                productionsToDo, consumptionsToDo,
                producerAdditionalTaskTime, consumerAdditionalTaskTime,
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

    private static void createThreads(int producers, int consumers,
                                      LinkedList<Thread> producerThreads, LinkedList<Thread> consumerThreads,
                                      int productionsToDo, int consumptionsToDo,
                                      int producerAdditionalTaskTime, int consumerAdditionalTaskTime,
                                      BufferProxy bufferProxy, int bufferSize) {
        for (int i = 0; i  < producers; i++) {
            producerThreads.add(new Thread(new Producer(i, bufferProxy, bufferSize / 2, productionsToDo, producerAdditionalTaskTime)));
        }
        for (int i = 0; i  < consumers; i++) {
            consumerThreads.add(new Thread(new Consumer(i, bufferProxy, bufferSize / 2, consumptionsToDo, consumerAdditionalTaskTime)));
        }
    }
}
