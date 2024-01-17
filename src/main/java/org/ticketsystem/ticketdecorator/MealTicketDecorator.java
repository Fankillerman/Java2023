package org.ticketsystem.ticketdecorator;

import org.ticketsystem.Ticket;

public class MealTicketDecorator extends TicketDecorator {

    public MealTicketDecorator(Ticket decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", with Meal";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 20;
    }
}
