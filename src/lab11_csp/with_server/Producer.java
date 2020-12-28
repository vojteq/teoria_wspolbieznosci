package lab11_csp.with_server;

import lab11_csp.util.Color;
import lab11_csp.util.Utils;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;
import java.util.Random;

public class Producer implements CSProcess {

    private final int id;
    private final One2OneChannelInt serverRequestChannel;
    private final One2OneChannelInt serverResponseChannel;
    private final ArrayList<One2OneChannelInt> bufferChannels;

    public Producer(int id, One2OneChannelInt serverRequestChannel, One2OneChannelInt serverResponseChannel) {
        this.id = id;
        this.serverRequestChannel = serverRequestChannel;
        this.serverResponseChannel = serverResponseChannel;
        this.bufferChannels = new ArrayList<>();
    }

    @Override
    public void run() {
        Random random = new Random();
        int productionsDone = 0;
        while(true) {
            serverRequestChannel.out().write(1);
            int bufferIndex = serverResponseChannel.in().read();
            int producedValue = Math.abs(random.nextInt() % 10) + 1;
            bufferChannels.get(bufferIndex).out().write(producedValue);
            Utils.print("(P" + id + " <" + (productionsDone++) + ">) produced: " + producedValue + ", to B" + bufferIndex, Color.BLUE);
            Utils.sleep(100);
        }
    }

    public void addChannel(One2OneChannelInt one2OneChannelInt) {
        bufferChannels.add(one2OneChannelInt);
    }
}
