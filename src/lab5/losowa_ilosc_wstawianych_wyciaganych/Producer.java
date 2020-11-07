package lab5.losowa_ilosc_wstawianych_wyciaganych;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer implements Runnable {
    private Monitor monitor;
    private Random random;
    private int maxElems;

    public Producer(Monitor monitor, int maxElems) {
        this.monitor = monitor;
        random = new Random();
        this.maxElems = maxElems;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.produce(produce((random.nextInt() % maxElems) / 2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Integer> produce(int size) {
        List<Integer> products = new ArrayList<>();
        for (int i = 0; i < size; i++)
            products.add(1);
        return products;
    }
}
