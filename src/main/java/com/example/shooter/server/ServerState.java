package com.example.shooter.server;

import java.util.ArrayList;
import java.util.List;

public class ServerState {
    public List<String> playerNames = new ArrayList<>();
    public List<Boolean> playersReady = new ArrayList<>();
    public List<Integer> playerScores = new ArrayList<>();


    public boolean gameIsStarted = false;

    public double bigTargetY;
    public double smallTargetY;



}
