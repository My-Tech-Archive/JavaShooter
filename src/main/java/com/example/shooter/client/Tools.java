package com.example.shooter.client;

public class Tools {

    public static Boolean isCollided(double arrowX, double arrowY,
                              double targetX, double targetY, double targetRadius) {

        double distance = Math.sqrt(Math.pow(arrowX-targetX, 2) + Math.pow(arrowY-targetY, 2));
        return distance < targetRadius;
    }
}
