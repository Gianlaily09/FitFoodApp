public class Makanan {
    private int id;
    private String namaMakanan;
    private int kalori;
    private String tanggal;

    // Constructor untuk insert data baru
    public Makanan(String namaMakanan, int kalori) {
        this.namaMakanan = namaMakanan;
        this.kalori = kalori;
    }

    // Constructor lengkap dengan id & tanggal (untuk ambil dari database)
    public Makanan(int id, String namaMakanan, int kalori, String tanggal) {
        this.id = id;
        this.namaMakanan = namaMakanan;
        this.kalori = kalori;
        this.tanggal = tanggal;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public String getNamaMakanan() { return namaMakanan; }
    public int getKalori() { return kalori; }
    public String getTanggal() { return tanggal; }
}