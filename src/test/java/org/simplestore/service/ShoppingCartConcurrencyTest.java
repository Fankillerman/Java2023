package org.simplestore.service;

import org.junit.jupiter.api.Test;
import org.simplestore.model.Inventory;
import org.simplestore.model.Product;
import org.simplestore.model.ProductNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShoppingCartConcurrencyTest {
    private final Inventory inventory = new Inventory();

    @Test
    void addAndRemoveItemsConcurrently() throws InterruptedException {
        ShoppingCart shoppingCart = new ShoppingCart(inventory);
        inventory.addProduct(new Product(1, "Test Product", 10.0));

        int threadCount = 10;
        int addCount = 100;
        int removeCount = 50;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount / 2; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < addCount / (threadCount / 2); j++) {
                    shoppingCart.addItem(1, 1);
                }
            });
            threads[i].start();
        }

        for (int i = threadCount / 2; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < removeCount / (threadCount / 2); j++) {
                    shoppingCart.removeItem(1, 1);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(50, shoppingCart.getItemQuantity(1));
    }


    @Test
    void calculateTotalCostConcurrently() throws InterruptedException, ProductNotFoundException {
        ShoppingCart shoppingCart = new ShoppingCart(inventory);
        inventory.addProduct(new Product(1, "Test Product", 10.0));

        int threadCount = 10;
        int addCount = 100;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < addCount / threadCount; j++) {
                    shoppingCart.addItem(1, 1);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(1000.0, shoppingCart.calculateTotalCost());
    }


    // Note for presenter: Discuss the importance of concurrency testing in a multi-threaded environment.
}
