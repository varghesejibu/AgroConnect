package dao;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    private static String URL = "jdbc:mysql://localhost:3306/agroconnect?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String USER = "root";
    private static String PASS = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties");
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                URL = p.getProperty("db.url", URL);
                USER = p.getProperty("db.user", USER);
                PASS = p.getProperty("db.pass", PASS);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
