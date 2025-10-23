package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the cart.
     * If the item already exists, it just updates the quantity.
     */
    public void addItem(Produce produce, int quantity) {
        // Check if item is already in the cart
        for (CartItem item : items) {
            if (item.getProduce().getId() == produce.getId()) {
                // If it is, just add to the quantity
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If it's not in the cart, add it as a new item
        items.add(new CartItem(produce, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Calculates the grand total for the entire cart.
     */
    public double calculateGrandTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getItemTotal();
        }
        return total;
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        items.clear();
    }

    /**
     * Checks if the cart is empty.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}