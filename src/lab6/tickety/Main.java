package lab6.tickety;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Monitor monitor = new Monitor(10);
        int no_producers = 2, no_consumers = 2;
        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < no_producers; i++)
            producers.add(new Thread(new Producer(monitor, buffer)));
        for (int i = 0; i < no_consumers; i++)
            consumers.add(new Thread(new Consumer(monitor, buffer)));

        for (Thread producer : producers)
            producer.start();
        for (Thread consumer : consumers)
            consumer.start();

    }
}
