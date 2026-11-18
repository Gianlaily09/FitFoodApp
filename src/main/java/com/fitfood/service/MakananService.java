package com.fitfood.service;

import com.fitfood.model.Makanan;
import com.fitfood.repository.MakananRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MakananService {

    private final MakananRepository makananRepository;

    @Autowired
    public MakananService(MakananRepository makananRepository) {
        this.makananRepository = makananRepository;
    }

    @Transactional
    public void tambahMakanan(String namaMakanan, int kalori) {
        Makanan makanan = new Makanan(namaMakanan, kalori);
        makananRepository.save(makanan);
    }

    @Transactional(readOnly = true)
    public List<Makanan> ambilSemuaMakanan() {
        return makananRepository.findAllOrderByIdDesc();
    }
}
