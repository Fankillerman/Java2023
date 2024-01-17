package org.ticketsystem;

public class BasicTicket implements Ticket {
    private final double price;

    public BasicTicket(double price) {
        this.price = price;
    }

    @Override
    public String getDescription() {
        return "Basic Ticket";
    }

    @Override
    public double getPrice() {
        return this.price;
    }
}
