package lab5.tickety;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Monitor monitor = new Monitor(1);
        int no_producers = 2, no_consumers = 2;
        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < no_producers; i++)
            producers.add(new Thread(new Producer(monitor)));
        for (int i = 0; i < no_consumers; i++)
            consumers.add(new Thread(new Consumer(monitor)));

        for (Thread producer : producers)
            producer.start();
        for (Thread consumer : consumers)
            consumer.start();

    }
}
