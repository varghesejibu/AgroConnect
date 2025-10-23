package model;

import java.util.ArrayList;
import java.util.List;

public class Buyer extends User {
    private List<Order> orderList = new ArrayList<>();
    private Cart cart; // 1. Added a Cart

    public Buyer(int id, String username, String password) {
        super(id, username, password, "Buyer");
        this.cart = new Cart(); // 2. Initialized the cart
    }

    /**
     * 3. Added a getter for the cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * 4. This new method converts cart items into orders.
     * Your dashboard should call this when the user clicks "Yes" on the
     * order summary popup.
     */
    public void placeOrderFromCart() {
        // Loop through all items in the cart
        for (CartItem item : cart.getItems()) {
            Produce produce = item.getProduce();
            int quantity = item.getQuantity();
            double total = item.getItemTotal();
            
            // Create an Order object based on your old logic.
            // Note: Using orderList.size() + 1 for an ID is not ideal,
            // but it matches your previous code.
            Order newOrder = new Order(
                orderList.size() + 1, 
                this.getId(), // buyerId
                produce.getId(), // produceId
                quantity, 
                total
            );
            
            // Add the new order to the buyer's order history
            this.orderList.add(newOrder);
        }
        
        // 5. Finally, clear the cart.
        this.cart.clearCart();
    }

    /**
     * Returns the list of past orders.
     */
    public List<Order> getOrders() {
        return orderList;
    }
}