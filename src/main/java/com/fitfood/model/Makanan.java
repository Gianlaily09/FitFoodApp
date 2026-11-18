package com.fitfood.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "catatan_makanan")
public class Makanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nama_makanan", nullable = false, length = 100)
    private String namaMakanan;

    @Column(name = "kalori", nullable = false)
    private int kalori;

    @Column(name = "tanggal", insertable = false, updatable = false)
    private LocalDateTime tanggal;

    public Makanan() {
    }

    public Makanan(String namaMakanan, int kalori) {
        this.namaMakanan = namaMakanan;
        this.kalori = kalori;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }
}
