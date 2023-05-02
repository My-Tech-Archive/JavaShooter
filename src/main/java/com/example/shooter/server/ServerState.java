package com.example.shooter.server;

import com.example.shooter.database.Users;

import java.util.ArrayList;
import java.util.List;

public class ServerState {
    // Состояние сервера, которое постоянно отправляется клиентам

    public List<String> playerNames = new ArrayList<>();
    public List<Boolean> playersReady = new ArrayList<>();
    public List<Integer> playerScores = new ArrayList<>();
    public List<Integer> playerShots = new ArrayList<>();
    public List<Double> arrowsPositionX = new ArrayList<>();

    public List<Users> leaders = new ArrayList<>();

    public String winner = "";


    public boolean gameIsStarted = false;

    public double bigTargetY;
    public double smallTargetY;



}
