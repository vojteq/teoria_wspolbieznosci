package lab6.tickety;

public class Ticket {
    private final int id;

    Ticket(int id) {
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
