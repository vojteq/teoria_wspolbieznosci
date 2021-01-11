package lab11_csp.with_server_tests;

import lab11_csp.util.Utils;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.nio.file.Path;

public class Main {
    public static long MAX_TIME = 100000;     //ms
    public static int OPERATION_TIME = 100;
    public static final String RESULT_FILE_NAME = System.getProperty("user.dir") + "\\src\\lab11_csp\\with_server_tests\\distBufferResults.txt";

    public static Parallel parallel;

    public static void main(String[] args) {
        int[] producers = {1, 2, 4, 8, 16};
        int[] consumers = {1, 2, 4, 8, 16};
        int[] buffers = {1, 2, 4, 5, 10, 20};
        int index = 4;

        int noProducers = producers[index];
        int noConsumers = consumers[index];
        int noBuffers = 4;
        CSProcess[] processes = new CSProcess[noProducers + noConsumers + noBuffers + 1];       // ostatnie miejsce na serwer

        init(processes, noProducers, noConsumers, noBuffers);
        parallel = new Parallel(processes);
        parallel.run();
    }

    private static void init(CSProcess[] processes, int noProducers, int noConsumers, int noBuffers) {
        int serverIndex = noProducers + noConsumers + noBuffers;
        for (int i = 0; i < noBuffers; i++) {
            processes[i + noProducers + noConsumers] = new Buffer(i);
        }
        processes[serverIndex] = new Server(noBuffers);

        for (int i = 0; i < noProducers; i++) {
            One2OneChannelInt serverRequestChannel = Channel.one2oneInt();
            One2OneChannelInt serverResponseChannel = Channel.one2oneInt();
            processes[i] = new Producer(i, serverRequestChannel, serverResponseChannel);
            ((Server) processes[serverIndex]).addProducerChannels(serverRequestChannel, serverResponseChannel);
            for (int j = 0; j < noBuffers; j++) {
                One2OneChannelInt producerChannel = Channel.one2oneInt();
                ((Producer) processes[i]).addChannel(producerChannel);
                ((Buffer) processes[j + noProducers + noConsumers]).addProducerChannel(producerChannel);
            }
        }

        for (int i = 0; i < noConsumers; i++) {
            One2OneChannelInt serverRequestChannel = Channel.one2oneInt();
            One2OneChannelInt serverResponseChannel = Channel.one2oneInt();
            processes[i + noProducers] = new Consumer(i, serverRequestChannel, serverResponseChannel);
            ((Server) processes[serverIndex]).addConsumerChannels(serverRequestChannel, serverResponseChannel);
            for (int j = 0; j < noBuffers; j++) {
                One2OneChannelInt requestChannel = Channel.one2oneInt();
                One2OneChannelInt responseChannel = Channel.one2oneInt();
                ((Consumer) processes[i + noProducers]).addChannels(requestChannel, responseChannel);
                ((Buffer) processes[j + noProducers + noConsumers]).addConsumerChannels(requestChannel, responseChannel);
            }
        }
    }
}
