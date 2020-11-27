package lab7.active_object;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        int bufferSize = 10;
        int producers = 2;
        int consumers = 2;
        Scheduler scheduler = new Scheduler();
        BufferProxy bufferProxy = new BufferProxy(bufferSize, scheduler);
        LinkedList<Thread> producerThreads = new LinkedList<>();
        LinkedList<Thread> consumerThreads = new LinkedList<>();

        createThreads(producers, producerThreads, consumers, consumerThreads, bufferProxy);
        Thread schedulerThread = new Thread(scheduler::run);
        schedulerThread.start();
        for (Thread thread : producerThreads) {
            thread.start();
        }
        for (Thread thread : consumerThreads) {
            thread.start();
        }
    }

    private static void createThreads(int producers, LinkedList<Thread> producerThreads, int consumers, LinkedList<Thread> consumerThreads, BufferProxy bufferProxy) {
        Random random = new Random();
        for (int i = 0; i  < producers; i++) {
            producerThreads.add(new Thread(() -> {
                while (true) {
                    FutureIntegerList futureIntegerList = bufferProxy.produceFuture(random.nextInt());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (futureIntegerList.isDone()) {
                        try {
                            System.out.println(futureIntegerList.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }));
        }
        for (int i = 0; i  < consumers; i++) {
            consumerThreads.add(new Thread(() -> {
                while (true) {
                    FutureIntegerList futureIntegerList = bufferProxy.produceFuture(random.nextInt());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (futureIntegerList.isDone()) {
                        try {
                            System.out.println(futureIntegerList.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }));
        }
    }
}
