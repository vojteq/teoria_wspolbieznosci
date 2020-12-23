package lab11_csp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;
import org.w3c.dom.css.CSSPageRule;

public class Main {
    public static void main(String[] args) {
        int noProducers = 2;
        int noConsumers = 2;
        One2OneChannelInt[] producerChannels = new One2OneChannelInt[noProducers];
        One2OneChannelInt[] consumerChannels = new One2OneChannelInt[noConsumers];
        One2OneChannelInt[] consumerRequestChannels = new One2OneChannelInt[noConsumers];
        CSProcess[] processes = new CSProcess[noProducers + noConsumers + 1];

        init(producerChannels, consumerChannels, consumerRequestChannels, processes);
        Parallel parallel = new Parallel(processes);
        parallel.run();
    }

    private static void init(One2OneChannelInt[] producerChannels, One2OneChannelInt[] consumerChannels,
                             One2OneChannelInt[] consumerRequestChannels, CSProcess[] processes) {
        // creating channels
        for (int i = 0; i < producerChannels.length; i++) {
            producerChannels[i] = Channel.one2oneInt();
        }
        for (int i = 0; i < consumerChannels.length; i++) {
            consumerChannels[i] = Channel.one2oneInt();
            consumerRequestChannels[i] = Channel.one2oneInt();
        }

        // creating processes
        for (int i = 0; i < producerChannels.length; i++) {
            processes[i] = new Producer(producerChannels[i]);
        }
        for (int i = 0; i < producerChannels.length; i++) {
            processes[producerChannels.length + i] = new Consumer(consumerChannels[i], consumerRequestChannels[i]);
        }
        processes[producerChannels.length + consumerChannels.length] = new Buffer(producerChannels, consumerChannels, consumerRequestChannels);
    }
}
