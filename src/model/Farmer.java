package model;

import dao.DBConnection; // Import your DB connection class
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Farmer extends User {
    private List<Produce> produceList = new ArrayList<>();

    public Farmer(int id, String username, String password) {
        super(id, username, password, "Farmer");
    }

    /**
     * This method saves the produce to the MySQL database
     * AND adds it to the local list for the UI to refresh.
     */
    public void addProduce(String name, double price) throws IllegalArgumentException, SQLException {
        // 1. Validation
        if (name == null || name.isEmpty() || price <= 0) {
            throw new IllegalArgumentException("Invalid produce details: Name cannot be empty and price must be positive.");
        }

        // 2. Database Insertion
        String sql = "INSERT INTO produce (name, price, farmer_id) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBConnection.getConnection(); // Use your connection class
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, this.getId()); // Get the farmer's ID from the User class

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating produce failed, no rows affected.");
            }

            // 3. Get the new 'id' from the database
            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newProduceId = generatedKeys.getInt(1); // This is the auto-incremented ID
                
                // 4. Create the Produce object with the *correct* DB ID
                Produce newProduce = new Produce(newProduceId, name, price, this.getId());
                
                // 5. Add to the in-memory list so the UI can refresh
                this.produceList.add(newProduce);
                
            } else {
                throw new SQLException("Creating produce failed, no ID obtained.");
            }
        } catch (SQLException e) {
            // Re-throw so the UI's catch block can display it
            throw new SQLException("Database error: " + e.getMessage(), e);
        } finally {
            // 6. Clean up resources
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { /* ignore */ }
            if (ps != null) try { ps.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    /**
     * Gets the farmer's current list of produce.
     */
    public List<Produce> getProduceList() {
        return produceList;
    }
    
    /**
     * Sets the farmer's produce list. 
     * Used to populate the list from the database on login.
     */
    public void setProduceList(List<Produce> produceList) {
        this.produceList = produceList;
    }

    /**
     * Removes a produce item from the farmer's in-memory list.
     */
    public void removeProduce(Produce produce) {
        if (produce != null) {
            this.produceList.remove(produce);
        }
    }
}