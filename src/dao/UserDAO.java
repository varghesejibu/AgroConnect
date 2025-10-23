package dao;

import java.sql.*;
import model.*;

public class UserDAO {
    public boolean registerUser(String username, String password, String role) throws SQLException {
        String q = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String q = "SELECT 1 FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
