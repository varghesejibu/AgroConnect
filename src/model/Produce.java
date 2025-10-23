package model;

import java.util.Date;

public class Produce {
    private int id;
    private String name;
    private double price;
    private int farmerId;
    private Date listedAt;

    public Produce(int id, String name, double price, int farmerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.farmerId = farmerId;
        this.listedAt = new Date();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public String getDetails() {
        return name + " - ₹" + price;
    }
}
