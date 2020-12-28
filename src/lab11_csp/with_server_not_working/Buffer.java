package lab11_csp.with_server_not_working;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {

    private final One2OneChannelInt[] producerChannels;
    private final One2OneChannelInt[] consumerResponseChannels;
    private final One2OneChannelInt[] consumerRequestChannels;

    public Buffer(One2OneChannelInt[] producerChannels, One2OneChannelInt[] consumerResponseChannels, One2OneChannelInt[] consumerRequestChannels) {
        this.producerChannels = producerChannels;
        this.consumerResponseChannels = consumerResponseChannels;
        this.consumerRequestChannels = consumerRequestChannels;
    }

    @Override
    public void run() {
        Guard[] guards = new Guard[producerChannels.length + consumerRequestChannels.length];
        for (int i = 0; i < producerChannels.length; i++) {
            guards[i] = producerChannels[i].in();
        }
        for (int i = 0; i < consumerRequestChannels.length; i++) {
            guards[producerChannels.length + i] = consumerRequestChannels[i].in();
        }
        Alternative alternative = new Alternative(guards);
        int aliveProducers = producerChannels.length;
        int aliveConsumers = consumerRequestChannels.length;
        int value = -2;
        while (aliveProducers > 0 && aliveConsumers > 0) {
            int index = alternative.select();
            if (index < producerChannels.length) {
                value = producerChannels[index].in().read();
                if (value == -1) {
                    aliveProducers--;
                }
            }
            else if (value != -2){
                if (consumerRequestChannels[index].in().read() == -1) {
                    aliveConsumers--;
                }
                else {
                    consumerResponseChannels[index].out().write(value);
                    value = -2;
                }
            }
        }
    }
}
