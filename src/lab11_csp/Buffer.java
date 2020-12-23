package lab11_csp;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {

    private final One2OneChannelInt[] producerChannels;
    private final One2OneChannelInt[] consumerChannels;
    private final One2OneChannelInt[] consumerRequestChannels;

    public Buffer(One2OneChannelInt[] producerChannels, One2OneChannelInt[] consumerChannels, One2OneChannelInt[] consumerRequestChannels) {
        this.producerChannels = producerChannels;
        this.consumerChannels = consumerChannels;
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
        int count = producerChannels.length + consumerRequestChannels.length;
        while (count > 0) {
            int index = alternative.select();
            if (index < producerChannels.length) {
                boolean spaceAvailable = true;
                if (spaceAvailable) {

                }
            }
            else {

            }
        }
    }
}
