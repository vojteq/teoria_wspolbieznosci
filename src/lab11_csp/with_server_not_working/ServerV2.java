package lab11_csp.with_server_not_working;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class ServerV2 implements CSProcess {

    private final One2OneChannelInt[] producerRequestChannels;
    private final One2OneChannelInt[] producerResponseChannels;
    private final One2OneChannelInt[] consumerRequestChannels;
    private final One2OneChannelInt[] consumerResponseChannels;
    private final int numberOfBuffers;

    public ServerV2(One2OneChannelInt[] producerRequestChannels, One2OneChannelInt[] producerResponseChannels,
                    One2OneChannelInt[] consumerRequestChannels, One2OneChannelInt[] consumerResponseChannels,
                    int numberOfBuffers) {
        this.producerRequestChannels = producerRequestChannels;
        this.producerResponseChannels = producerResponseChannels;
        this.consumerRequestChannels = consumerRequestChannels;
        this.consumerResponseChannels = consumerResponseChannels;
        this.numberOfBuffers = numberOfBuffers;
    }

    @Override
    public void run() {
        int producerCounterForBuffers = 0;
        int consumerCounterForBuffers = 0;    // liczniki do odsyłania indeksu bufora

        Guard[] guards = new Guard[producerRequestChannels.length + consumerRequestChannels.length];
        for (int i = 0; i < producerRequestChannels.length; i++) {
            guards[i] = producerRequestChannels[i].in();
        }
        for (int i = 0; i < consumerRequestChannels.length; i++) {
            guards[i + producerRequestChannels.length] = consumerRequestChannels[i].in();
        }
        Alternative alternative = new Alternative(guards);
        int aliveProducersCounter = producerRequestChannels.length;
        int aliveConsumersCounter = consumerRequestChannels.length;
        int index;
        while (aliveProducersCounter > 0 && aliveConsumersCounter > 0) {
            System.out.println("server started");
            index = alternative.select();
            System.out.println("prod index " + index);
            if (index < producerRequestChannels.length) {       // producenci
                if (producerRequestChannels[index].in().read() == -1) {
                    aliveProducersCounter--;
                    System.out.println("producer" + index + " finished");
                }
                else {
                    producerResponseChannels[index].out().write(producerCounterForBuffers);
                    System.out.println("prod written " + producerCounterForBuffers);
                    producerCounterForBuffers = (producerCounterForBuffers + 1) % numberOfBuffers;
                }
            }
            else {      //konsumenci
                if (consumerRequestChannels[index].in().read() == -1) {
                    aliveConsumersCounter--;
                    System.out.println("consumer" + index + " finished");
                }
                else {
                    consumerResponseChannels[index].out().write(consumerCounterForBuffers);
                    consumerCounterForBuffers = (consumerCounterForBuffers + 1) % numberOfBuffers;
                }
            }

        }
    }
}
