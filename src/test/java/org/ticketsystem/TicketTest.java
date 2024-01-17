package org.ticketsystem;

import org.junit.jupiter.api.Test;
import org.ticketsystem.ticketdecorator.MealTicketDecorator;
import org.ticketsystem.ticketdecorator.VIPSeatDecorator;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    public void testBasicTicket() {
        Ticket basicTicket = new BasicTicket(100.0);
        assertEquals("Basic Ticket", basicTicket.getDescription());
        assertEquals(100.0, basicTicket.getPrice());
    }

    @Test
    public void testMealTicketDecorator() {
        Ticket basicTicket = new BasicTicket(100.0);
        Ticket mealTicket = new MealTicketDecorator(basicTicket);
        assertEquals("Basic Ticket, with Meal", mealTicket.getDescription());
        assertEquals(120.0, mealTicket.getPrice()); // Assuming meal adds 20.0 to the price
    }

    @Test
    public void testVIPSeatDecorator() {
        Ticket basicTicket = new BasicTicket(100.0);
        Ticket vipTicket = new VIPSeatDecorator(basicTicket);
        assertEquals("Basic Ticket, VIP Seat", vipTicket.getDescription());
        assertEquals(150.0, vipTicket.getPrice()); // Assuming VIP seat adds 50.0 to the price
    }

    @Test
    public void testCombinedDecorators() {
        Ticket basicTicket = new BasicTicket(100.0);
        Ticket decoratedTicket = new MealTicketDecorator(new VIPSeatDecorator(basicTicket));
        assertEquals("Basic Ticket, VIP Seat, with Meal", decoratedTicket.getDescription());
        assertEquals(170.0, decoratedTicket.getPrice()); // Combined additional cost of meal and VIP seat
    }
}
