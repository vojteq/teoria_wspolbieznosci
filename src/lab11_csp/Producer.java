package lab11_csp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Producer implements CSProcess {

    private final One2OneChannelInt one2OneChannelInt;

    public Producer(One2OneChannelInt one2OneChannelInt) {
        this.one2OneChannelInt = one2OneChannelInt;
    }

    @Override
    public void run() {

    }
}
