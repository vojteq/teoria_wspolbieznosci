package lab11_csp.with_server_tests;

import lab11_csp.util.Utils;
import org.jcsp.lang.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements CSProcess {

    private final int numberOfBuffers;
    private final ArrayList<One2OneChannelInt> producerRequestChannels;
    private final ArrayList<One2OneChannelInt> producerResponseChannels;
    private final ArrayList<One2OneChannelInt> consumerRequestChannels;
    private final ArrayList<One2OneChannelInt> consumerResponseChannels;

    private int[] prodResults;
    private int[] consResults;

    public Server(int numberOfBuffers) {
        this.numberOfBuffers = numberOfBuffers;
        this.producerRequestChannels = new ArrayList<>();
        this.producerResponseChannels = new ArrayList<>();
        this.consumerRequestChannels = new ArrayList<>();
        this.consumerResponseChannels = new ArrayList<>();
    }

    @Override
    public void run() {
        int noProducers = producerRequestChannels.size();
        int noConsumers = consumerRequestChannels.size();

        this.prodResults = new int[noProducers];
        this.consResults = new int[noConsumers];

        int producerCounterForBuffers = 0;
        int consumerCounterForBuffers = 0;    // liczniki do odsyłania indeksu bufora

        Guard[] guards = new Guard[noProducers + consumerRequestChannels.size()];
        for (int i = 0; i < noProducers; i++) {
            guards[i] = producerRequestChannels.get(i).in();
        }
        for (int i = 0; i < noConsumers; i++) {
            guards[i + noProducers] = consumerRequestChannels.get(i).in();
        }
        Alternative alternative = new Alternative(guards);
        int index;
        long startTime = new Date().getTime();
        while ((new Date().getTime()) - startTime < Main.MAX_TIME) {
            index = alternative.select();
            if (index < noProducers) {       // producenci
                prodResults[index] = producerRequestChannels.get(index).in().read();
                producerResponseChannels.get(index).out().write(producerCounterForBuffers);
                producerCounterForBuffers = (producerCounterForBuffers + 1) % numberOfBuffers;
            }
            else {      //konsumenci
                index -= noProducers;
                consResults[index] = consumerRequestChannels.get(index).in().read();
                consumerResponseChannels.get(index).out().write(consumerCounterForBuffers);
                consumerCounterForBuffers = (consumerCounterForBuffers + 1) % numberOfBuffers;
            }
        }
        writeResults();
    }

    private void writeResults() {
        int producers = producerRequestChannels.size();
        int consumers = consumerRequestChannels.size();
        int avgProductions = 0, avgConsumptions = 0;

        for (int i = 0; i < producers; i++) {
            avgProductions += prodResults[i];
        }
        avgProductions /= producers;

        for (int i = 0; i < consumers; i++) {
            avgConsumptions += consResults[i];
        }
        avgConsumptions /= consumers;

        try {
            FileWriter writer = new FileWriter(Main.RESULT_FILE_NAME, true);
            String output = "P: " + producers + ", C: " + consumers + ", B: " + numberOfBuffers + "\n" +
                    "avg productions done: " + avgProductions + "\n" +
                    "avg consumptions done: " + avgConsumptions + "\n\n";
            writer.write(output);
            writer.close();
            System.out.println("writing done");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while writing");
        }
        System.exit(1234);

//        Parallel.destroy();
//        Main.parallel.releaseAllThreads();
//        Parallel.destroy();
    }

    public void addProducerChannels(One2OneChannelInt requestChannel, One2OneChannelInt responseChannel) {
        producerRequestChannels.add(requestChannel);
        producerResponseChannels.add(responseChannel);
    }

    public void addConsumerChannels(One2OneChannelInt requestChannel, One2OneChannelInt responseChannel) {
        consumerRequestChannels.add(requestChannel);
        consumerResponseChannels.add(responseChannel);
    }
}
