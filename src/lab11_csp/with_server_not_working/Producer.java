package lab11_csp.with_server_not_working;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Random;

public class Producer implements CSProcess {

    private final One2OneChannelInt serverRequestChannel;
    private final One2OneChannelInt serverResponseChannel;
    private final One2OneChannelInt[] bufferChannels;

    public Producer(One2OneChannelInt serverRequestChannel, One2OneChannelInt serverResponseChannel,
                    One2OneChannelInt[] bufferChannels) {
        this.serverRequestChannel = serverRequestChannel;
        this.serverResponseChannel = serverResponseChannel;
        this.bufferChannels = bufferChannels;
    }

    @Override
    public void run() {
        Random random = new Random();
        while(true) {
            serverRequestChannel.out().write(1);
            System.out.println("server requested");
            int bufferIndex = serverResponseChannel.in().read();
            System.out.println("server response: " + bufferIndex);
            int producedValue = Math.abs(random.nextInt() % 10) + 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bufferChannels[bufferIndex].out().write(producedValue);
        }
    }
}
