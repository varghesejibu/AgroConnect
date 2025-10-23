package service;

import model.*;
import dao.DBConnection;
import java.sql.*;

public class AuthService {
    public AuthService() {
      
    }

    public User login(String username, String password) throws Exception {
        String q = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new Exception("User not found");
                String dbPass = rs.getString("password");
                if (!dbPass.equals(password)) throw new Exception("Invalid password");
                int id = rs.getInt("id");
                String role = rs.getString("role");
                if ("Farmer".equalsIgnoreCase(role)) return new Farmer(id, username, password);
                else return new Buyer(id, username, password);
            }
        } catch (SQLException e) {
            throw new Exception("Database error during login: " + e.getMessage(), e);
        }
    }
}
