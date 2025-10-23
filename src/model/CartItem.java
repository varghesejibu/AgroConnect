package model;

public class CartItem {
    private Produce produce;
    private int quantity;

    public CartItem(Produce produce, int quantity) {
        this.produce = produce;
        this.quantity = quantity;
    }

    public Produce getProduce() {
        return produce;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the total price for this cart item.
     * @return price * quantity
     */
    public double getItemTotal() {
        return produce.getPrice() * quantity;
    }
}