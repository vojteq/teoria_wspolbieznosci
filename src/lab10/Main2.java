package lab10;

import org.jcsp.lang.*;

class Producer2 implements CSProcess
{ private One2OneChannelInt channel;
    private int start;
    public Producer2 (final One2OneChannelInt out, int start)
    { channel = out;
        this.start = start;
    } // constructor
    public void run ()
    { int item;
        for (int k = 0; k < 100; k++)
        { item = (int)(Math.random()*100)+1+start;
            channel.out().write(item);
        } // for
        channel.out().write(-1);
        System.out.println("Producer" + start + " ended.");
    } // run
} // class Producer2



class Consumer2 implements CSProcess {
    private One2OneChannelInt in;
    private One2OneChannelInt req;

    public Consumer2(final One2OneChannelInt req, final
    One2OneChannelInt in) {
        this.req = req;
        this.in = in;
    } // constructor

    public void run() {
        int item;
        while (true) {
            req.out().write(0); // Request data - blocks until data isavailable
            item = in.in().read();
            if (item < 0)
                break;
            System.out.println(item);
        } // for
        System.out.println("Consumer ended.");
    } // run
} // class Consume


class Buffer implements CSProcess {
    // Subscripts for buffer
    int hd = -1;
    int tl = -1;
    private One2OneChannelInt[] in; // Input from Producer
    private One2OneChannelInt[] req; // Request for data from Consumer
    private One2OneChannelInt[] out; // Output to Consumer
    // The buffer itself
    private int[] buffer = new int[10];

    public Buffer(final One2OneChannelInt[] in, final
    One2OneChannelInt[] req, final One2OneChannelInt[] out) {
        this.in = in;
        this.req = req;
        this.out = out;
    } // constructor

    public void run() {
        final Guard[] guards = {in[0].in(), in[1].in(), req[0].in(), req[1].in()};
        final Alternative alt = new Alternative(guards);
        int countdown = 4; // Number of processes running
        while (countdown > 0) {
            int index = alt.select();
            switch (index) {
                case 0:
                case 1: // A Producer is ready to send
                    if (hd < tl + 11) // Space available
                    {
                        int item = in[index].in().read();
                        if (item < 0)
                            countdown--;
                        else {
                            hd++;
                            buffer[hd % buffer.length] = item;
                        }
                    }
                    break;
                case 2:
                case 3: // A Consumer is ready to read
                    if (tl < hd) // Item(s) available
                    {
                        req[index - 2].in().read(); // Read and discard request
                        tl++;
                        int item = buffer[tl % buffer.length];
                        out[index - 2].out().write(item);
                    } else if (countdown <= 2) // Signal consumer to end
                    {
                        req[index - 2].in().read(); // Read and discard request
                        out[index - 2].out().write(-1); // Signal end
                        countdown--;
                    }
                    break;
            } // switch
        } // while
        System.out.println("Buffer ended.");
    } // run
} // class Buff


final class Main2 {
    public Main2() { // Create channel objects
        final One2OneChannelInt[] prodChan = {
                Channel.one2oneInt(), Channel.one2oneInt()}; // Producers
        final One2OneChannelInt[] consReq = {
                Channel.one2oneInt(), Channel.one2oneInt()}; // Consumer requests
        final One2OneChannelInt[] consChan = {
                Channel.one2oneInt(), Channel.one2oneInt()}; // Consumer data
        // Create parallel construct
        CSProcess[] procList = {new Producer2(prodChan[0], 0),
                new Producer2(prodChan[1], 100),
                new Buffer(prodChan, consReq,
                        consChan),
                new Consumer2(consReq[0],
                        consChan[0]),
                new Consumer2(consReq[1],
                        consChan[1])}; // Processes
        Parallel par = new Parallel(procList); // PAR construct
        par.run(); // Execute processes in parallel
    } // PCMain constructor

    public static void main(String[] args) {
        new Main2();
    } // main
} // class PCMain2

