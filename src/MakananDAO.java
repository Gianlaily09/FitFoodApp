import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MakananDAO {

    // Method untuk menyimpan data makanan baru
    public boolean simpanMakanan(Makanan makanan) {
        String sql = "INSERT INTO catatan_makanan (nama_makanan, kalori) VALUES (?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, makanan.getNamaMakanan());
            pstmt.setInt(2, makanan.getKalori());
            pstmt.executeUpdate();
            return true;
            
        } catch (Exception e) {
            System.err.println("Gagal menyimpan data: " + e.getMessage());
            return false;
        }
    }

    // Method untuk mengambil seluruh daftar makanan dari database
    public List<Makanan> ambilSemuaMakanan() {
        List<Makanan> daftarMakanan = new ArrayList<>();
        String sql = "SELECT * FROM catatan_makanan ORDER BY id DESC";
        
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama_makanan");
                int kalori = rs.getInt("kalori");
                String tanggal = rs.getString("tanggal");
                
                daftarMakanan.add(new Makanan(id, nama, kalori, tanggal));
            }
            
        } catch (Exception e) {
            System.err.println("Gagal memuat data: " + e.getMessage());
        }
        
        return daftarMakanan;
    }
}