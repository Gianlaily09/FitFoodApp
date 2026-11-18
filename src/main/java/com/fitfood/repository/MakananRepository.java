package com.fitfood.repository;

import com.fitfood.model.Makanan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MakananRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public MakananRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Makanan makanan) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(makanan);
    }

    public List<Makanan> findAllOrderByIdDesc() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Makanan ORDER BY id DESC", Makanan.class).list();
    }
}
