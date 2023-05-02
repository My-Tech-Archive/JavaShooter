package com.example.shooter.database;
public class Test {


    public static void main(String[] args) {

        Database.WriteUser("max", 7);
        var list = Database.GetUsers();

        System.out.println(list.size());
        for (var user: list)
        {
            System.out.println(user.getName() + " " + user.getWins());
        }

    }
}
