package lab5.losowa_ilosc_wstawianych_wyciaganych.is_first;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int noProducers = 3, noConsumers = 3, maxBufSize = 20;
        Monitor monitor = new Monitor(maxBufSize);
        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < noProducers; i++)
            producers.add(new Thread(new Producer(i, monitor, maxBufSize / 2)));
        for (int i = 0; i < noConsumers; i++)
            consumers.add(new Thread(new Consumer(i, monitor, maxBufSize / 2)));

        for (Thread producer : producers)
            producer.start();
        for (Thread consumer : consumers)
            consumer.start();

    }
}
