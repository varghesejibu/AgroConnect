package dao;

import model.Produce;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduceDAO {

    
    public List<Produce> getAllProduce() throws SQLException {
        List<Produce> produceList = new ArrayList<>();
        String sql = "SELECT * FROM produce"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                produceList.add(new Produce(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("farmer_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return produceList;
    }

    // --- (Keep your existing getProduceByFarmerId() method here) ---
    public List<Produce> getProduceByFarmerId(int farmerId) throws SQLException {
        List<Produce> produceList = new ArrayList<>();
        String sql = "SELECT * FROM produce WHERE farmer_id = ?"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    produceList.add(new Produce(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), farmerId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return produceList;
    }

    
    public void deleteProduceById(int produceId) throws SQLException {
        String sql = "DELETE FROM produce WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, produceId);
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                // This isn't an error, it just means the item was already gone
                System.out.println("No produce found with ID: " + produceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}