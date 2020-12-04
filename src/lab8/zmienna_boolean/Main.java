package lab8.zmienna_boolean;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int bufferSize = 20;
        int noProducers = 10;
        int noConsumers = 10;
        int productionsToDo = 100;
        int consumptionsToDo = 100;
        int productionDelay = 10;
        int consumptionDelay = 10;
        int producerAdditionalTaskTime = 10;
        int consumerAdditionalTaskTime = 10;
        Monitor monitor = new Monitor(bufferSize, productionDelay, consumptionDelay);
        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < noProducers; i++)
            producers.add(new Thread(new Producer(i, monitor, bufferSize / 2, productionsToDo, producerAdditionalTaskTime)));
        for (int i = 0; i < noConsumers; i++)
            consumers.add(new Thread(new Consumer(i, monitor, bufferSize / 2, consumptionsToDo, consumerAdditionalTaskTime)));

        for (Thread producer : producers)
            producer.start();
        for (Thread consumer : consumers)
            consumer.start();

    }
}
