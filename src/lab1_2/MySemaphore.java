package lab1_2;

public class MySemaphore {
    private boolean value;

    public MySemaphore() {
        value = false;
    }

    public synchronized void acquire() throws InterruptedException {
        while (value)
            wait();
        value = true;
    }

    public synchronized void release() {
        value = false;
        notify();
    }
}
