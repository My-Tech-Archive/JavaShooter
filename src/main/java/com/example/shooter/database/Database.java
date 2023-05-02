package com.example.shooter.database;

import javafx.application.Application;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


import java.io.Console;
import java.io.File;
import java.util.List;
import java.util.Objects;


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


    public static List<Users> GetUsers()
    {
        System.out.println("Get users request");
        Session session = sessionFactory.openSession();

        var userList = session.createQuery("from Users u order by u.wins DESC", Users.class).getResultList();

        session.close();
        return userList;
    }

    public static int GetWins(String username)
    {
        System.out.println("Get wins request for name: " + username);
        boolean userExists = false;
        int wins = 0;

        Session session = sessionFactory.openSession();

        List<Users> userList = session.createQuery("from Users", Users.class).getResultList();

        for (Users user : userList)
            if (Objects.equals(user.getName(), username))
            {
                wins = user.getWins();
                userExists = true;
            }

        if (userExists)
        {
            session.close();
            return wins;
        }

        // Если пользователя не существует - создаем новую запись
        session.getTransaction().begin();

        Users user = new Users();
        user.setName(username);
        user.setWins(0);
        session.save(user);
        System.out.println("New user " + username + ": " + wins);

        session.getTransaction().commit();
        session.close();

        return 0;
    }




    public static void WriteUser(String username, int wins)
    {
        System.out.println("Write: " + username);
        Session session = sessionFactory.openSession();

        session.getTransaction().begin();

        boolean exists = false;

        List<Users> userList = session.createQuery("from Users", Users.class).getResultList();

        for (Users user : userList)
            if (Objects.equals(user.getName(), username))
                exists = true;

        if (exists)
        {
            Query query = session.createQuery("update Users set wins = :wins where name = :username");
            query.setParameter("wins", wins);
            query.setParameter("username", username);
            query.executeUpdate();
            System.out.println("Updated " + username + ": " + wins);
        }
        else
        {
            Users user = new Users();
            user.setName(username);
            user.setWins(wins);
            session.save(user);
            System.out.println("New user " + username + ": " + wins);
        }


        session.getTransaction().commit();
        session.close();
    }




}
