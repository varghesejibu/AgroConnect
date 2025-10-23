package dao;

import model.*;
import java.sql.*;
import java.util.*;

public class FarmerDAO {
    public void addProduce(Produce p) throws SQLException {
        String query = "INSERT INTO produce(name, price, farmer_id) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
        }
    }

    public List<Produce> getProduceByFarmer(int farmerId) throws SQLException {
        List<Produce> list = new ArrayList<>();
        String query = "SELECT * FROM produce WHERE farmer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, farmerId);
            ResultSet rs = ps.executeQuery();
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
}
