package lab11_csp.with_server_not_working;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements CSProcess {

    private final One2OneChannelInt serverRequestChannel;
    private final One2OneChannelInt serverResponseChannel;
    private final One2OneChannelInt[] bufferRequestChannels;
    private final One2OneChannelInt[] bufferResponseChannels;

    public Consumer(One2OneChannelInt serverRequestChannel, One2OneChannelInt serverResponseChannel,
                    One2OneChannelInt[] bufferRequestChannels, One2OneChannelInt[] bufferResponseChannels) {
        this.serverRequestChannel = serverRequestChannel;
        this.serverResponseChannel = serverResponseChannel;
        this.bufferRequestChannels = bufferRequestChannels;
        this.bufferResponseChannels = bufferResponseChannels;
    }

    @Override
    public void run() {
        while (true) {
            serverRequestChannel.out().write(1);
            int bufferIndex = serverResponseChannel.in().read();
            bufferRequestChannels[bufferIndex].out().write(1);
            int consumedValue = bufferResponseChannels[bufferIndex].in().read();
            System.out.println(consumedValue);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
