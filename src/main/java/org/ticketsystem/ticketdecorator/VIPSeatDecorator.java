package org.ticketsystem.ticketdecorator;

import org.ticketsystem.Ticket;

public class VIPSeatDecorator extends TicketDecorator {

    public VIPSeatDecorator(Ticket decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", VIP Seat";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 50;
    }
}
