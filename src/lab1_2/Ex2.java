package lab1_2;

public class Ex2 {
    private int value;
    private int n;
    private static final MySemaphore mySemaphore = new MySemaphore();

    public Ex2(int value, int n) {
        this.value = value;
        this.n = n;
    }

    void execute() throws InterruptedException {
        for (int i = 0; i < this.n; i++) {
            mySemaphore.acquire();
            increment();
            mySemaphore.release();

            mySemaphore.acquire();
            decrement();
            mySemaphore.release();
        }
    }

    void increment() {
        value++;
    }

    void decrement() {
        value--;
    }

    void show() {
        System.out.println("value: " + value);
    }
}
