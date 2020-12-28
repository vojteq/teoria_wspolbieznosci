package lab11_csp.with_server_not_working;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

public class Main {
    public static void main(String[] args) {
        int noProducers = 4;
        int noConsumers = 4;
        int noBuffers = 2;
        One2OneChannelInt[] producerServerRequestChannels = new One2OneChannelInt[noProducers];
        One2OneChannelInt[] producerServerResponseChannels = new One2OneChannelInt[noProducers];
        One2OneChannelInt[][] producerBufferChannels = new One2OneChannelInt[noProducers][noBuffers];
        One2OneChannelInt[] consumerServerRequestChannels = new One2OneChannelInt[noConsumers];
        One2OneChannelInt[] consumerServerResponseChannels = new One2OneChannelInt[noConsumers];
        One2OneChannelInt[][] consumerBufferRequestChannels = new One2OneChannelInt[noConsumers][noBuffers];
        One2OneChannelInt[][] consumerBufferResponseChannels = new One2OneChannelInt[noConsumers][noBuffers];
        CSProcess[] processes = new CSProcess[noProducers + noConsumers + noBuffers + 1];


        init(producerServerRequestChannels,
                producerServerResponseChannels,
                producerBufferChannels,
                consumerServerRequestChannels,
                consumerServerResponseChannels,
                consumerBufferRequestChannels,
                consumerBufferResponseChannels,
                processes,
                noProducers,
                noConsumers,
                noBuffers);
        Parallel parallel = new Parallel(processes);
        parallel.run();
    }

    private static void init(One2OneChannelInt[] producerServerRequestChannels,
                             One2OneChannelInt[] producerServerResponseChannels,
                             One2OneChannelInt[][] producerBufferChannels,
                             One2OneChannelInt[] consumerServerRequestChannels,
                             One2OneChannelInt[] consumerServerResponseChannels,
                             One2OneChannelInt[][] consumerBufferRequestChannels,
                             One2OneChannelInt[][] consumerBufferResponseChannels,
                             CSProcess[] processes,
                             int noProducers,
                             int noConsumers,
                             int noBuffers) {
        for (int  i = 0; i < noProducers; i++) {
            producerServerRequestChannels[i] = Channel.one2oneInt();
            producerServerResponseChannels[i] = Channel.one2oneInt();
            for (int j = 0; j < noBuffers; j++) {
                producerBufferChannels[i][j] = Channel.one2oneInt();
            }
        }
        for (int i = 0; i < noConsumers; i++) {
            consumerServerRequestChannels[i] = Channel.one2oneInt();
            consumerServerResponseChannels[i] = Channel.one2oneInt();
            for (int j = 0; j < noBuffers; j++) {
                consumerBufferRequestChannels[i][j] = Channel.one2oneInt();
                consumerBufferResponseChannels[i][j] = Channel.one2oneInt();
            }
        }

        for (int i = 0; i < noProducers; i++) {
            processes[i] = new Producer(
                    producerServerRequestChannels[i],
                    producerServerResponseChannels[i],
                    producerBufferChannels[i]
            );
        }
        for (int i = 0; i < noConsumers; i++) {
            processes[i + noProducers] = new Consumer(
                    consumerServerRequestChannels[i],
                    consumerServerResponseChannels[i],
                    consumerBufferRequestChannels[i],
                    consumerBufferResponseChannels[i]
            );
        }
        for (int i = 0; i < noBuffers; i++) {
            One2OneChannelInt[] tmpProducerBufferChannels = new One2OneChannelInt[noProducers];
            for (int j = 0; j < noProducers; j++) {
                tmpProducerBufferChannels[j] = producerBufferChannels[j][i];
            }
            One2OneChannelInt[] tmpConsumerRequestBufferChannels = new One2OneChannelInt[noConsumers];
            One2OneChannelInt[] tmpConsumerResponseBufferChannels = new One2OneChannelInt[noConsumers];
            for (int j = 0; j < noConsumers; j++) {
                tmpConsumerRequestBufferChannels[j] = consumerBufferRequestChannels[j][i];
                tmpConsumerResponseBufferChannels[j] = consumerBufferResponseChannels[j][i];
            }
            processes[i + noProducers + noConsumers] = new Buffer(
                    tmpProducerBufferChannels,
                    tmpConsumerRequestBufferChannels,
                    tmpConsumerResponseBufferChannels
            );
        }
        processes[noProducers + noConsumers + noBuffers] = new ServerV2(
                producerServerRequestChannels,
                producerServerRequestChannels,
                consumerServerRequestChannels,
                consumerServerResponseChannels,
                noBuffers
        );
    }


//    private static void init(One2OneChannelInt[] producerChannels, One2OneChannelInt[] consumerChannels,
//                             One2OneChannelInt[] consumerRequestChannels, CSProcess[] processes) {
//        // creating channels
//        for (int i = 0; i < producerChannels.length; i++) {
//            producerChannels[i] = Channel.one2oneInt();
//        }
//        for (int i = 0; i < consumerChannels.length; i++) {
//            consumerChannels[i] = Channel.one2oneInt();
//            consumerRequestChannels[i] = Channel.one2oneInt();
//        }
//
//        // creating processes
//        for (int i = 0; i < producerChannels.length; i++) {
//            processes[i] = new Producer(producerChannels[i]);
//        }
//        for (int i = 0; i < producerChannels.length; i++) {
//            processes[producerChannels.length + i] = new Consumer(consumerChannels[i], consumerRequestChannels[i]);
//        }
//        processes[producerChannels.length + consumerChannels.length] = new Buffer(producerChannels, consumerChannels, consumerRequestChannels);
//    }
}
