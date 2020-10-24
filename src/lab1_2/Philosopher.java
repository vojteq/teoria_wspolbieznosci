package lab1_2;

import java.util.ArrayList;

public class Philosopher implements Runnable {
    private final int number;
    private ArrayList<MySemaphore> forks;

    public Philosopher(int number, ArrayList<MySemaphore> forks) {
        this.number = number;
        this.forks = forks;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void run() {
        while (true) {
            try {
                forks.get(number).acquire();
                forks.get((number + 1) % 5).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("philosopher " + number + ": eating");
            forks.get(number).release();
            forks.get((number + 1) % 5).release();
        }
    }
}
