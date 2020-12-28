package lab11_csp.with_server;

import lab11_csp.util.Color;
import lab11_csp.util.Utils;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Consumer implements CSProcess {

    private final int id;
    private final One2OneChannelInt serverRequestChannel;
    private final One2OneChannelInt serverResponseChannel;
    private final ArrayList<One2OneChannelInt> bufferRequestChannels;
    private final ArrayList<One2OneChannelInt> bufferResponseChannels;

    public Consumer(int id, One2OneChannelInt serverRequestChannel, One2OneChannelInt serverResponseChannel) {
        this.id = id;
        this.serverRequestChannel = serverRequestChannel;
        this.serverResponseChannel = serverResponseChannel;
        this.bufferRequestChannels = new ArrayList<>();
        this.bufferResponseChannels = new ArrayList<>();
    }

    @Override
    public void run() {
        int consumptionsDone = 0;
        while (true) {
            serverRequestChannel.out().write(1);
            int bufferIndex = serverResponseChannel.in().read();
            bufferRequestChannels.get(bufferIndex).out().write(1);
            int consumedValue = bufferResponseChannels.get(bufferIndex).in().read();
            Utils.print("(C" + id + " <" + (consumptionsDone++) + ">) consumed: " + consumedValue + ", from B" + bufferIndex, Color.GREEN);
            Utils.sleep(100);
        }
    }

    public void addChannels(One2OneChannelInt requestChannel, One2OneChannelInt responseChannel) {
        bufferRequestChannels.add(requestChannel);
        bufferResponseChannels.add(responseChannel);
    }
}
