package lab8.zmienna_boolean;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int bufferSize = 20;
        int threads = 4;
        int operationsToDo = 100;
        int monitorOperationDelay = 20;
        int additionalTask = 50;
        Monitor monitor = new Monitor(bufferSize, monitorOperationDelay, monitorOperationDelay);
        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            producers.add(new Thread(new Producer(i, monitor, bufferSize / 2, operationsToDo, additionalTask)));
            consumers.add(new Thread(new Consumer(i, monitor, bufferSize / 2, operationsToDo, additionalTask)));
        }

        for (Thread producer : producers)
            producer.start();
        for (Thread consumer : consumers)
            consumer.start();

    }
}
