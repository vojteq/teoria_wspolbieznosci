package lab1_2;

import lab3.Consumer;
import lab3.Monitor;
import lab3.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        for (int i = 0; i < noThreads; i++) {
//            final int n = i;
//            Thread thread = new Thread(() -> System.out.println("thread" + n));
//            thread.start();
//        }
//
//        Thread.sleep(1000);
//        System.out.println("\n\n");
//
//        for (int i = 0; i < noThreads; i++) {
//            new MyThread(i).start();
//        }

//        List<Thread> threads = new ArrayList<>();
//        int noThreads = 100;
//        lab1_2.Ex2 ex2 = new lab1_2.Ex2(0, 1000);
//        for (int i = 0; i < noThreads; i++) {
//            Thread thread = new Thread(() -> {
//                try {
//                    ex2.execute();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            threads.add(thread);
//        }
//
//        for (Thread thread : threads) {
//            thread.start();
//        }
//
//        for (Thread thread : threads) {
//            thread.join();
//        }
//
//        ex2.show();

        // lab 2
        ArrayList<lab1_2.MySemaphore> forks = new ArrayList<>();
        ArrayList<lab1_2.Philosopher> philosophers = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            forks.add(new lab1_2.MySemaphore());
            philosophers.add(new lab1_2.Philosopher(i, forks));
            threads.add(new Thread(philosophers.get(i)));
        }


        for (int i = 0; i < 5; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < 5; i++) {
            threads.get(i).join();
        }

    }

    static class MyThread extends Thread {
        private final int val;

        public MyThread(int val) {
            this.val = val;
        }

        @Override
        public void run() {
            System.out.println("myThread" + val);
        }
    }
}
