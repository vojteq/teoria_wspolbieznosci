package lab10;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.util.Random;

class Consumer1 implements CSProcess {
    One2OneChannelInt channel;

    Consumer1(One2OneChannelInt channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(channel.in().read());
        }
    }
}

class Producer1 implements CSProcess {
    One2OneChannelInt channel;

    Producer1(One2OneChannelInt channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            channel.out().write(random.nextInt() % 10);
        }
    }
}

class Buffer1 implements CSProcess {
    One2OneChannelInt fromProd;
    One2OneChannelInt toCons;

    Buffer1(One2OneChannelInt fromProd, One2OneChannelInt toCons) {
        this.fromProd = fromProd;
        this.toCons = toCons;
    }

    @Override
    public void run() {
        int value;
        while (true) {
            value = fromProd.in().read();
            toCons.out().write(value);
        }
    }
}

public class Main1Buffer {
    public static void main(String[] args) {
        One2OneChannelInt prod = Channel.one2oneInt();
        One2OneChannelInt cons = Channel.one2oneInt();

        CSProcess[] processes = {
                new Producer1(prod),
                new Consumer1(cons),
                new Buffer1(prod, cons)
        };

        Parallel parallel = new Parallel(processes);
        parallel.run();
    }

}
