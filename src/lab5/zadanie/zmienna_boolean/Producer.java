package lab5.zadanie.zmienna_boolean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer implements Runnable {
    private final int id;
    private final Monitor monitor;
    private final Random random;
    private final int maxElements;
    private int counter;

    public Producer(int id, Monitor monitor, int maxElements) {
        counter = 0;
        this.id = id;
        this.monitor = monitor;
        random = new Random();
        this.maxElements = maxElements;
    }

    @Override
    public void run() {
        while (true) {
            try {
//                zakleszczenia / normalnie
                monitor.produce(this, produce(Math.abs(random.nextInt() % maxElements) + 1));
//                koniec zakleszczen / normalnego

//                zagladzanie
//                if (id == 0) {
//                    monitor.produce(this, produce(maxElements));
//                }
//                else {
//                    monitor.produce(this, produce(1));
//                }
//                koniec zagladzania
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Integer> produce(int size) {
        List<Integer> products = new ArrayList<>();
        for (int i = 0; i < size; i++)
            products.add(Math.abs(random.nextInt() % 9) + 1);
        return products;
    }

    public int count() {
        return ++counter;
    }

    public int getId() {
        return id;
    }
}
