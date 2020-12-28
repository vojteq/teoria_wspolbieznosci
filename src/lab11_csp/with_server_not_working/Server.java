package lab11_csp.with_server_not_working;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Server implements CSProcess {

    private final One2OneChannelInt[] producerRequestChannels;
    private final One2OneChannelInt[] producerResponseChannels;
    private final One2OneChannelInt[] consumerRequestChannels;
    private final One2OneChannelInt[] consumerResponseChannels;
    private final int numberOfBuffers;

    public Server(One2OneChannelInt[] producerRequestChannels, One2OneChannelInt[] producerResponseChannels,
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

        Guard[] producerGuards = new Guard[producerRequestChannels.length];
        for (int i = 0; i < producerRequestChannels.length; i++) {
            producerGuards[i] = producerRequestChannels[i].in();
        }
        Guard[] consumerGuards = new Guard[consumerRequestChannels.length];
        for (int i = 0; i < consumerRequestChannels.length; i++) {
            consumerGuards[i] = consumerRequestChannels[i].in();
        }
        Alternative producerAlternative = new Alternative(producerGuards);
        Alternative consumerAlternative = new Alternative(consumerGuards);
        int aliveProducersCounter = producerRequestChannels.length;
        int aliveConsumersCounter = consumerRequestChannels.length;
        int index;
        while (aliveProducersCounter > 0 && aliveConsumersCounter > 0) {
            System.out.println("server started");
            index = producerAlternative.select();
            System.out.println("prod index " + index);
            if (producerRequestChannels[index].in().read() == -1) {
                aliveProducersCounter--;
                System.out.println("producer" + index + " finished");
            }
            else {
                producerResponseChannels[index].out().write(producerCounterForBuffers);
                System.out.println("prod written " + producerCounterForBuffers);
                producerCounterForBuffers = (producerCounterForBuffers + 1) % numberOfBuffers;
            }

            index = consumerAlternative.select();
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
