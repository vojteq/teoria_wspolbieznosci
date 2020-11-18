package lab6.tickety;

public class ConsumerTicket {
    private final int id;

    ConsumerTicket(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }
}
