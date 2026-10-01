package datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDbConnection {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/autopesula?user=appuser&password=password");
            } catch (SQLException e) {
                System.out.println("Yhteys tietokantaan epäonnistui.");
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void terminate() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
