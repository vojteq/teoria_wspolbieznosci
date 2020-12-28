package lab11_csp.without_server;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

public class Main {
    public static void main(String[] args) {
        int noProducers = 2;
        int noConsumers = 2;
        int noBuffers = 2;
        CSProcess[] processes = new CSProcess[noProducers + noConsumers + noBuffers];

        init(processes, noProducers, noConsumers, noBuffers);
        Parallel parallel = new Parallel(processes);
        parallel.run();
    }

    private static void init(CSProcess[] processes, int noProducers, int noConsumers, int noBuffers) {
        for (int i = 0; i < noBuffers; i++) {
            processes[i + noProducers + noConsumers] = new Buffer(i);
        }

        for (int i = 0; i < noProducers; i++) {
            processes[i] = new Producer(i);
            for (int j = 0; j < noBuffers; j++) {
                One2OneChannelInt producerChannel = Channel.one2oneInt();
                ((Producer) processes[i]).addChannel(producerChannel);
                ((Buffer) processes[j + noProducers + noConsumers]).addProducerChannel(producerChannel);
            }
        }

        for (int i = 0; i < noConsumers; i++) {
            processes[i + noProducers] = new Consumer(i);
            for (int j = 0; j < noBuffers; j++) {
                One2OneChannelInt requestChannel = Channel.one2oneInt();
                One2OneChannelInt responseChannel = Channel.one2oneInt();
                ((Consumer) processes[i + noProducers]).addChannels(requestChannel, responseChannel);
                ((Buffer) processes[j + noProducers + noConsumers]).addConsumerChannels(requestChannel, responseChannel);
            }
        }
    }
}
