package com.example.shooter.server;

import com.example.shooter.ClientRequests;
import com.example.shooter.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static int sleepTime = 5;
    public static int clientSleepTime = 5;

    int port = 8001;
    public ServerModel serverModel = new ServerModel();
    public ArrayList<ClientRequests> clientRequests = new ArrayList<>();
    ServerChannel serverChannel;

    double targetSpeed = 1;
    double arrowSpeed = 7;
    double arrowRightLimitX = 850;
    double arrowStartPosX = 90;

    int highTargetLimitY = 50;
    int lowTargetLimitY = 620;

    boolean bigTargetMoveUp = false;
    double bigTargetRadius = 55;
    double bigTargetPosX = 715;
    boolean smallTargetMoveUp = false;
    double smallTargetRadius = 30;
    double smallTargetPosX = 838;
    int winScore = 5;

    ArrayList<Boolean> arrowsLaunched = new ArrayList<>();
    public double[] arrowsPositionY = new double[]{ 278, 375, 476, 174 };



    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }

    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");

        new Thread(()->{
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    Connection connection = new Connection(clientSocket);
                    if (serverChannel == null) {
                        serverChannel = new ServerChannel(this);
                        serverChannel.start();
                    }

                    serverChannel.connections.add(connection);
                    System.out.println("Client connected");
                }
                catch (IOException e) { }
            }

        }).start();

        reset();

        // Собственный поток для обновления состояния игры
        while (true) {

            updateModel();
            if (serverModel.gameIsStarted) moveGame();

            if (serverChannel != null) serverChannel.send();

            try { Thread.sleep(Server.sleepTime); }
            catch (Exception e) { }
        }


    }

    private void moveGame() {
        bigTarget();
        smallTarget();
        arrowsMove();
        updateCollide();
    }


    private void updateCollide() {
        for (int i = 0; i < serverModel.arrowsPositionX.size(); i++) {
            double arrowPosX = serverModel.arrowsPositionX.get(i);
            boolean collidedToBig = Tools.isCollided(arrowPosX, arrowsPositionY[i],
                    bigTargetPosX, serverModel.bigTargetY, bigTargetRadius);
            boolean collidedToSmall = Tools.isCollided(arrowPosX, arrowsPositionY[i],
                    smallTargetPosX, serverModel.smallTargetY, smallTargetRadius);

            if (collidedToBig) {
                int newScore = serverModel.playerScores.get(i) + 1;
                serverModel.playerScores.set(i, newScore);

                if (newScore >= winScore) serverModel.winner = serverModel.playerNames.get(i);

                serverModel.arrowsPositionX.set(i, arrowStartPosX);
                arrowsLaunched.set(i, false);
                System.out.println("Big");
            }
            else if (collidedToSmall) {
                int newScore = serverModel.playerScores.get(i) + 2;
                serverModel.playerScores.set(i, newScore);

                if (newScore >= winScore) serverModel.winner = serverModel.playerNames.get(i);

                serverModel.arrowsPositionX.set(i, arrowStartPosX);
                arrowsLaunched.set(i, false);
                System.out.println("Small");
            }
        }
    }
    private void bigTarget() {
        if (bigTargetMoveUp) {
            serverModel.bigTargetY -= targetSpeed;
            if (serverModel.bigTargetY < highTargetLimitY) bigTargetMoveUp = false;
        }
        else {
            serverModel.bigTargetY += targetSpeed;
            if (serverModel.bigTargetY > lowTargetLimitY) {
                bigTargetMoveUp = true;
            }
        }
    }
    private void smallTarget() {
        if (smallTargetMoveUp) {
            serverModel.smallTargetY -= targetSpeed * 1.5;
            if (serverModel.smallTargetY < highTargetLimitY) smallTargetMoveUp = false;
        }
        else {
            serverModel.smallTargetY += targetSpeed  * 1.5;
            if (serverModel.smallTargetY > lowTargetLimitY) {
                smallTargetMoveUp = true;
            }
        }
    }
    private void arrowsMove() {
        for (int i = 0; i < arrowsLaunched.size(); i++) {
            if (arrowsLaunched.get(i)) {
                Double newPosition = serverModel.arrowsPositionX.get(i) + arrowSpeed;
                if (newPosition > arrowRightLimitX) {
                    arrowsLaunched.set(i, false);
                    newPosition = arrowStartPosX;
                }
                serverModel.arrowsPositionX.set(i, newPosition);
            }
        }
    }

    private void reset() {
        serverModel.smallTargetY = (highTargetLimitY + lowTargetLimitY)/2;
        serverModel.bigTargetY = (highTargetLimitY + lowTargetLimitY)/2;
        serverModel.winner = "";
        for (int i = 0; i < serverModel.playerScores.size(); i++) {
            serverModel.playerScores.set(i, 0);
            serverModel.playerShots.set(i, 0);
        }
    }


    private void updateModel() {
        boolean allPlayersReady = true;
        if (clientRequests.size() == 0) allPlayersReady = false;

        // Для каждого игрока
        for (int i = 0; i < clientRequests.size(); i++) {
            // В зависимости от количества игроков
            ClientRequests clientRequests = this.clientRequests.get(i);
            if (clientRequests == null) continue;

            if (serverModel.playerNames.size() == i) {

                // Заполнение листов данными от игроков
                if (serverModel.playerNames.contains(clientRequests.playerName))
                    serverModel.playerNames.add(clientRequests.playerName + "1");
                else serverModel.playerNames.add(clientRequests.playerName);

                serverModel.playersReady.add(clientRequests.isReady);
                serverModel.playerScores.add(0);
                serverModel.playerShots.add(0);
                serverModel.arrowsPositionX.add(arrowStartPosX);
                arrowsLaunched.add(false);
            }

            else  {

                serverModel.playersReady.set(i, clientRequests.isReady);
                if (clientRequests.shot) {
                    if (!arrowsLaunched.get(i)){
                        int newShots = serverModel.playerShots.get(i) + 1;
                        serverModel.playerShots.set(i, newShots);
                    }
                    arrowsLaunched.set(i, true);
                }
            }

            if (!clientRequests.isReady) allPlayersReady = false;
        }

        if (!allPlayersReady && serverModel.winner.length() > 0) reset();

        serverModel.gameIsStarted = allPlayersReady;
    }




}
