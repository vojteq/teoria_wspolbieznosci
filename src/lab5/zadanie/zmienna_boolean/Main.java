package lab5.zadanie.zmienna_boolean;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int noProducers = 10, noConsumers = 1, bufSize = 20;
        Monitor monitor = new Monitor(bufSize);
        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < noProducers; i++)
            producers.add(new Thread(new Producer(i, monitor, bufSize / 2)));
        for (int i = 0; i < noConsumers; i++)
            consumers.add(new Thread(new Consumer(i, monitor, bufSize / 2)));

        for (Thread producer : producers)
            producer.start();
        for (Thread consumer : consumers)
            consumer.start();

    }
}
