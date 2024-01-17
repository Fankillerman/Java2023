package org.ticketsystem;

import org.ticketsystem.ticketdecorator.MealTicketDecorator;
import org.ticketsystem.ticketdecorator.VIPSeatDecorator;

public class TicketReservationSystem {
    public static void main(String[] args) {
        // Create a basic ticket
        Ticket basicTicket = new BasicTicket(100.0);
        System.out.println("Basic Ticket: " + basicTicket.getDescription() + "; Price: " + basicTicket.getPrice());
        // Przy poprawnej implementacji, drukuje:
        //  Basic Ticket: Basic Ticket; Price: 100.0

        // Decorate the ticket with a meal
        Ticket mealTicket = new MealTicketDecorator(basicTicket);
        System.out.println("Meal Ticket: " + mealTicket.getDescription() + "; Price: " + mealTicket.getPrice());
        // Przy poprawnej implementacji, drukuje:
        //  Meal Ticket: Basic Ticket, with Meal; Price: 120.0

        // Further decorate the ticket with a VIP seat
        Ticket vipTicket = new VIPSeatDecorator(mealTicket);
        System.out.println("VIP Ticket: " + vipTicket.getDescription() + "; Price: " + vipTicket.getPrice());
        // Przy poprawnej implementacji, drukuje:
        //  VIP Ticket: Basic Ticket, with Meal, VIP Seat; Price: 170.0
    }
}
