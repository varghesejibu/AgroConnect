package dao;

import model.*;
import java.sql.*;
import java.util.*;

public class BuyerDAO {
    public List<Produce> getMarket() throws SQLException {
        List<Produce> list = new ArrayList<>();
        String query = "SELECT * FROM produce";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Produce(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("farmer_id")
                ));
            }
        }
        return list;
    }

    public void placeOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders(buyer_id, produce_id, quantity) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, order.getBuyerId());
            ps.setInt(2, order.getProduceId());
            ps.setInt(3, order.getQuantity());
            ps.executeUpdate();
        }
    }
}
