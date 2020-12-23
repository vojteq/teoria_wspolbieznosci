package lab10;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

class Consumer implements CSProcess {
    One2OneChannelInt channel;

    Consumer(One2OneChannelInt channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(channel.in().read());
        }
    }
}

class Producer implements CSProcess {
    One2OneChannelInt channel;

    Producer(One2OneChannelInt channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            channel.out().write(1);
        }
    }
}

public class MainNoBuffer {
    public static void main(String[] args) {
        One2OneChannelInt channel = Channel.one2oneInt();

        CSProcess[] processes = {
                new Producer(channel),
                new Consumer(channel)
        };

        Parallel parallel = new Parallel(processes);
        parallel.run();
    }

}
