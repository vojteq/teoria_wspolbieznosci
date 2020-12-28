package lab11_csp.without_server;

import lab11_csp.util.Color;
import lab11_csp.util.Utils;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.io.Console;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer implements CSProcess {

    private final int id;
    private final ArrayList<One2OneChannelInt> bufferChannels;

    public Producer(int id) {
        this.id = id;
        this.bufferChannels = new ArrayList<>();
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            int bufferIndex = Math.abs(random.nextInt() % bufferChannels.size());
            int producedValue = Math.abs(random.nextInt() % 10) + 1;
            Utils.print("(P" + id + ") trying to produce " + producedValue + " to B" + bufferIndex, Color.BLUE);
            bufferChannels.get(bufferIndex).out().write(producedValue);
            Utils.print("(P" + id + ") produced: " + producedValue + ", to B" + bufferIndex, Color.BLUE);
            Utils.sleep(100);
        }
    }

    public void addChannel(One2OneChannelInt one2OneChannelInt) {
        bufferChannels.add(one2OneChannelInt);
    }
}
