package lab11_csp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements CSProcess {

    private final One2OneChannelInt consumerChannel;
    private final One2OneChannelInt consumerRequestChannel;

    public Consumer(One2OneChannelInt consumerChannel, One2OneChannelInt consumerRequestChannel) {
        this.consumerChannel = consumerChannel;
        this.consumerRequestChannel = consumerRequestChannel;
    }

    @Override
    public void run() {

    }
}
