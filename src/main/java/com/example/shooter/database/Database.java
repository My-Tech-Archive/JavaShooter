package com.example.shooter.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


import java.io.File;


public class Database {

    private static final SessionFactory sessionFactory = initializeFactory();

    private static SessionFactory initializeFactory()
    {
        Configuration configuration = new Configuration().configure(new File("src\\main\\resources\\hibernate.cfg.xml"));
        configuration.addAnnotatedClass(Users.class);
        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }




    public static void WriteUser(String username, int wins)
    {
        Session session = sessionFactory.openSession();

        session.getTransaction().begin();

        Users user = new Users();
        user.setName(username);
        user.setWins(wins);

        session.save(user);
        session.getTransaction().commit();

        session.close();
    }




}
