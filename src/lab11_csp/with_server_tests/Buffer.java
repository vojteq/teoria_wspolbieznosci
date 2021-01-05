package lab11_csp.with_server_tests;

import lab11_csp.util.Color;
import lab11_csp.util.Utils;
import org.jcsp.lang.*;

import java.util.ArrayList;

public class Buffer implements CSProcess {

    private final int id;
    private final ArrayList<One2OneChannelInt> producerChannels;
    private final ArrayList<One2OneChannelInt> consumerRequestChannels;
    private final ArrayList<One2OneChannelInt> consumerResponseChannels;

    public Buffer(int id) {
        this.id = id;
        this.producerChannels = new ArrayList<>();
        this.consumerRequestChannels = new ArrayList<>();
        this.consumerResponseChannels = new ArrayList<>();
    }

    @Override
    public void run() {
        Guard[] producerGuards = new Guard[producerChannels.size()];
        Guard[] consumerGuards = new Guard[consumerResponseChannels.size()];
        for (int i = 0; i < producerChannels.size(); i++) {
            producerGuards[i] = producerChannels.get(i).in();
        }
        for (int i = 0; i < consumerRequestChannels.size(); i++) {
            consumerGuards[i] = consumerRequestChannels.get(i).in();
        }
        Alternative producerAlternative = new Alternative(producerGuards);
        Alternative consumerAlternative = new Alternative(consumerGuards);

        int productionsDone = 0;
        int consumptionsDone = 0;
        try {
            while (true) {
                int prodIndex = producerAlternative.select();
                int value = producerChannels.get(prodIndex).in().read();
                Utils.print("(B" + id + " <" + (productionsDone++) + ">) produced: " + value + ", by P" + prodIndex, Color.MAGENTA);

                int consIndex = consumerAlternative.select();
                consumerRequestChannels.get(consIndex).in().read();
                consumerResponseChannels.get(consIndex).out().write(value);
                Utils.print("(B" + id + " <" + (consumptionsDone++) + ">) consumed: " + value + ", by C" + consIndex, Color.RED);
            }
        }
        catch (Exception | ProcessInterruptedException ignored) {

        }
    }

    public void addProducerChannel(One2OneChannelInt producerChannel) {
        producerChannels.add(producerChannel);
    }

    public void addConsumerChannels(One2OneChannelInt requestChannel, One2OneChannelInt responseChannel) {
        consumerRequestChannels.add(requestChannel);
        consumerResponseChannels.add(responseChannel);
    }
}
