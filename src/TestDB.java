import java.sql.*;
public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/agroconnect?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root", "password"
            );
            System.out.println("✅ Connected successfully!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
