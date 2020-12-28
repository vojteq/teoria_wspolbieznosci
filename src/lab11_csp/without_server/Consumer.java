package lab11_csp.without_server;

import lab11_csp.util.Color;
import lab11_csp.util.Utils;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;
import java.util.Random;

public class Consumer implements CSProcess {

    private final int id;
    private final ArrayList<One2OneChannelInt> bufferRequestChannels;
    private final ArrayList<One2OneChannelInt> bufferResponseChannels;

    public Consumer(int id) {
        this.id = id;
        this.bufferRequestChannels = new ArrayList<>();
        this.bufferResponseChannels = new ArrayList<>();
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            int bufferIndex = Math.abs(random.nextInt() % bufferRequestChannels.size());
            Utils.print("(C" + id + ") requesting B" + bufferIndex, Color.GREEN);
            bufferRequestChannels.get(bufferIndex).out().write(1);
            Utils.print("(C" + id + ") waiting for response from B" + bufferIndex, Color.GREEN);
            int consumedValue = bufferResponseChannels.get(bufferIndex).in().read();
            Utils.print("(C" + id + ") consumed: " + consumedValue + ", from B" + bufferIndex, Color.GREEN);
            Utils.sleep(100);
        }
    }

    public void addChannels(One2OneChannelInt requestChannel, One2OneChannelInt responseChannel) {
        bufferRequestChannels.add(requestChannel);
        bufferResponseChannels.add(responseChannel);
    }
}
