package model;

import java.util.Date;

public class Order {
    private int id;
    private int buyerId;
    private int produceId;
    private int quantity;
    private double total;
    private Date orderDate;

    
    public Order(int id, int buyerId, int produceId, int quantity, double total) {
        this.id = id;
        this.buyerId = buyerId;
        this.produceId = produceId;
        this.quantity = quantity;
        this.total = total;
        this.orderDate = new Date();
    }

    public int getId() { return id; }
    public int getBuyerId() { return buyerId; }
    public int g`etProduceId() { return produceId; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
    public Date getOrderDate() { return orderDate; }
}