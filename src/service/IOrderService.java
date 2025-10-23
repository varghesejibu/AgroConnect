package service;

import model.Order;
import model.Produce;
import java.util.List;

public interface IOrderService {
    void placeOrder(Produce produce, int quantity);
    List<Order> getAllOrders();
}
