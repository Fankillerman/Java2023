package org.simplestore;

import org.simplestore.model.Inventory;
import org.simplestore.model.Product;
import org.simplestore.model.ProductNotFoundException;
import org.simplestore.service.ShoppingCart;
import org.simplestore.util.InventoryLoader;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        InventoryLoader.loadInventory("src/main/resources/inventory.txt", inventory);

        ShoppingCart shoppingCart = new ShoppingCart(inventory);

        shoppingCart.addItem(1, 2);
        shoppingCart.addItem(2, 1);

        System.out.println("Contents of the basket:");
        shoppingCart.getCartContents().forEach((productId, quantity) -> {
            try {
                Product product = inventory.getProduct(productId);
                System.out.println("Product: " + product.getName() + ", Quantity: " + quantity);
            } catch (ProductNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            }
        });

        shoppingCart.removeItem(2, 1);
        try {
            double totalCost = shoppingCart.calculateTotalCost();
            System.out.println("The total cost of the basket:" + totalCost);
        } catch (ProductNotFoundException e) {
            System.err.println("Error when calculating the cost: " + e.getMessage());
        }
        shoppingCart.clearCart();

        System.out.println("\nAll products in the inventory:");
        inventory.listAllProducts().forEach(System.out::println);
    }
}
