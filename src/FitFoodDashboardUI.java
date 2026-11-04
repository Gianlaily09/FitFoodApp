import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FitFoodDashboardUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    
    private int targetKalori = 2000;
    private JLabel lblKaloriMasuk, lblSisaKalori;
    private JTable tblHistory;
    private DefaultTableModel tableModel;

    public FitFoodDashboardUI() {
        setTitle("FitFood - Health & Nutrition Dashboard (XAMPP Integrated)");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(46, 125, 50));
        sidebarPanel.setPreferredSize(new Dimension(240, 650));
        sidebarPanel.setLayout(new GridLayout(7, 1, 0, 10));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblLogo = new JLabel("🍎 FitFood UI");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sidebarPanel.add(lblLogo);

        JButton btnHome = createSidebarButton("📊 Beranda & Ringkasan");
        JButton btnCalorie = createSidebarButton("🔥 Catat & Riwayat Makanan");
        JButton btnProfile = createSidebarButton("👤 Profil Pengguna");
        JButton btnLogout = createSidebarButton("🚪 Keluar");

        sidebarPanel.add(btnHome);
        sidebarPanel.add(btnCalorie);
        sidebarPanel.add(btnProfile);
        sidebarPanel.add(new JLabel()); 
        sidebarPanel.add(new JLabel()); 
        sidebarPanel.add(btnLogout);

        add(sidebarPanel, BorderLayout.WEST);

        // ================= CONTENT AREA =================
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        mainContentPanel.add(createHomePanel(), "HOME");
        mainContentPanel.add(createCaloriePanel(), "CALORIE");
        mainContentPanel.add(createProfilePanel(), "PROFILE");

        add(mainContentPanel, BorderLayout.CENTER);

        // Event Navigasi
        btnHome.addActionListener(e -> {
            loadDataFromDatabase();
            cardLayout.show(mainContentPanel, "HOME");
        });
        btnCalorie.addActionListener(e -> {
            loadDataFromDatabase();
            cardLayout.show(mainContentPanel, "CALORIE");
        });
        btnProfile.addActionListener(e -> cardLayout.show(mainContentPanel, "PROFILE"));
        btnLogout.addActionListener(e -> System.exit(0));

        // Load data awal saat aplikasi pertama kali dibuka
        loadDataFromDatabase();
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(56, 142, 60));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }

    // ================= BERANDA =================
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 248));

        JLabel title = new JLabel("Ringkasan Kesehatan Hari Ini");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsGrid.setBackground(new Color(245, 247, 248));

        lblKaloriMasuk = new JLabel("0 kkal", SwingConstants.CENTER);
        lblSisaKalori = new JLabel(targetKalori + " kkal", SwingConstants.CENTER);

        cardsGrid.add(createStatCard("Total Kalori Masuk (DB)", lblKaloriMasuk, new Color(224, 242, 254)));
        cardsGrid.add(createStatCard("Sisa Target Kalori", lblSisaKalori, new Color(220, 252, 231)));
        cardsGrid.add(createStatCard("Koneksi Database", new JLabel("XAMPP Active", SwingConstants.CENTER), new Color(254, 243, 199)));

        panel.add(cardsGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, JComponent content, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lblTitle, BorderLayout.NORTH);

        content.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // ================= PENCATAT KALORI & TABEL DB =================
    private JPanel createCaloriePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Form Input
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setBackground(Color.WHITE);

        JTextField txtFood = new JTextField(12);
        JTextField txtCal = new JTextField(8);
        JButton btnSubmit = new JButton("Simpan ke XAMPP");
        btnSubmit.setBackground(new Color(46, 125, 50));
        btnSubmit.setForeground(Color.WHITE);

        formPanel.add(new JLabel("Nama Makanan:"));
        formPanel.add(txtFood);
        formPanel.add(new JLabel("Kalori (kkal):"));
        formPanel.add(txtCal);
        formPanel.add(btnSubmit);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabel Riwayat Makanan dari MySQL
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama Makanan", "Kalori (kkal)", "Waktu Input"}, 0);
        tblHistory = new JTable(tableModel);
        panel.add(new JScrollPane(tblHistory), BorderLayout.CENTER);

        // Aksi Simpan ke Database
        btnSubmit.addActionListener(e -> {
            String nama = txtFood.getText().trim();
            String kalStr = txtCal.getText().trim();

            if (nama.isEmpty() || kalStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Harap isi semua kolom!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int kalori = Integer.parseInt(kalStr);
                
                // Query Insert ke MySQL
                Connection conn = Koneksi.getConnection();
                if (conn != null) {
                    String sql = "INSERT INTO catatan_makanan (nama_makanan, kalori) VALUES (?, ?)";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, nama);
                    pst.setInt(2, kalori);
                    pst.executeUpdate();
                    
                    pst.close();
                    conn.close();

                    JOptionPane.showMessageDialog(panel, "Berhasil disimpan ke XAMPP MySQL!");
                    txtFood.setText("");
                    txtCal.setText("");
                    
                    loadDataFromDatabase(); // Refresh data di UI
                } else {
                    JOptionPane.showMessageDialog(panel, "Gagal terhubung ke MySQL! Pastikan XAMPP aktif.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Kalori harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // ================= PROFIL PENGGUNA =================
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Aplikasi:"));
        panel.add(new JLabel("FitFood Health Management System"));
        panel.add(new JLabel("Database Backend:"));
        panel.add(new JLabel("MySQL (XAMPP - localhost:3306)"));
        panel.add(new JLabel("Target Kalori Harian:"));
        panel.add(new JLabel(targetKalori + " kkal"));

        return panel;
    }

    // ================= SINKRONISASI DATA DARI MYSQL =================
    private void loadDataFromDatabase() {
        if (tableModel == null) return;
        
        tableModel.setRowCount(0); // Kosongkan tabel GUI
        int totalKalori = 0;

        try {
            Connection conn = Koneksi.getConnection();
            if (conn != null) {
                String sql = "SELECT * FROM catatan_makanan ORDER BY id DESC";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nama = rs.getString("nama_makanan");
                    int kalori = rs.getInt("kalori");
                    String tanggal = rs.getString("tanggal");

                    totalKalori += kalori;
                    tableModel.addRow(new Object[]{id, nama, kalori, tanggal});
                }

                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat data dari DB: " + e.getMessage());
        }

        // Update Tampilan Beranda
        lblKaloriMasuk.setText(totalKalori + " kkal");
        lblSisaKalori.setText((targetKalori - totalKalori) + " kkal");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FitFoodDashboardUI().setVisible(true);
        });
    }
}