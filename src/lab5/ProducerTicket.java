package lab5;

public class ProducerTicket {
    private final int id;

    ProducerTicket(int id) {
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
