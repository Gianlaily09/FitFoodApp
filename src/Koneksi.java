import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/fitfood_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; // Default user XAMPP
    private static final String PASSWORD = ""; // Default password XAMPP (kosong)

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi ke XAMPP gagal: " + e.getMessage());
        }
        return conn;
    }
}