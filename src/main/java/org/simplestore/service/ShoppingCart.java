package org.simplestore.service;

import org.simplestore.model.Inventory;
import org.simplestore.model.Product;
import org.simplestore.model.ProductNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    private final Inventory inventory;
    private final Map<Integer, Integer> cartItems = new ConcurrentHashMap<>();

    public ShoppingCart(Inventory inventory) {
        this.inventory = inventory;
    }

    public void addItem(int productId, int quantity) {
        cartItems.merge(productId, quantity, Integer::sum);
    }

    public int getItemQuantity(int productId) {
        return cartItems.getOrDefault(productId, 0);
    }

    public void removeItem(int productId, int quantity) {
        cartItems.computeIfPresent(productId, (id, currQuantity) -> {
            int newQuantity = currQuantity - quantity;
            return Math.max(newQuantity, 0);
        });
    }


    public double calculateTotalCost() throws ProductNotFoundException {
        double totalCost = 0;
        for (Map.Entry<Integer, Integer> entry : cartItems.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = inventory.getProduct(productId);
            totalCost += product.getPrice() * quantity;
        }
        return totalCost;
    }



    public void clearCart() {
        cartItems.clear();
    }
    public Map<Integer, Integer> getCartContents() {
        return new ConcurrentHashMap<>(cartItems);
    }

}
