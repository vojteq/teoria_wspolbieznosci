package lab11_csp.with_server;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Server implements CSProcess {

    private final int numberOfBuffers;
    private final ArrayList<One2OneChannelInt> producerRequestChannels;
    private final ArrayList<One2OneChannelInt> producerResponseChannels;
    private final ArrayList<One2OneChannelInt> consumerRequestChannels;
    private final ArrayList<One2OneChannelInt> consumerResponseChannels;

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
        int aliveProducersCounter = noProducers;
        int aliveConsumersCounter = noConsumers;
        int index;
        while (aliveProducersCounter > 0 && aliveConsumersCounter > 0) {
            index = alternative.select();
            if (index < noProducers) {       // producenci
                if (producerRequestChannels.get(index).in().read() == -1) {
                    aliveProducersCounter--;
                    System.out.println("producer" + index + " finished");
                }
                else {
                    producerResponseChannels.get(index).out().write(producerCounterForBuffers);
                    producerCounterForBuffers = (producerCounterForBuffers + 1) % numberOfBuffers;
                }
            }
            else {      //konsumenci
                if (consumerRequestChannels.get(index - noProducers).in().read() == -1) {
                    aliveConsumersCounter--;
                    System.out.println("consumer" + (index - noProducers) + " finished");
                }
                else {
                    consumerResponseChannels.get(index - noProducers).out().write(consumerCounterForBuffers);
                    consumerCounterForBuffers = (consumerCounterForBuffers + 1) % numberOfBuffers;
                }
            }

        }
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
