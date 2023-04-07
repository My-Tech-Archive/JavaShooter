package com.example.shooter.server;

import com.example.shooter.ClientState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static int sleepTime = 5;

    int port = 8001;
    public ServerState serverState = new ServerState();
    public ArrayList<ClientState> clientStates = new ArrayList<>();
    ServerDialog serverDialog;

    double targetSpeed = 1;
    double arrowSpeed = 2;
    double arrowRightLimitX = 700;
    double arrowStartPosX = 40;

    int highTargetLimitY = 50;
    int lowTargetLimitY = 650;

    boolean bigTargetMoveUp = false;
    double bigTargetRadius = 50;
    boolean smallTargetMoveUp = false;
    double smallTargetRadius = 25;

    ArrayList<Boolean> arrowsLaunched = new ArrayList<>();



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
                    if (serverDialog == null) {
                        serverDialog = new ServerDialog(this);
                        serverDialog.start();
                    }
                    serverDialog.connections.add(connection);
                    System.out.println("Client connected");
                }
                catch (IOException e) { }
            }

        }).start();

        while (true) {
            updateServerState();
            if (serverState.gameIsStarted) handleGame();
            if (serverDialog != null) serverDialog.send();

            try { Thread.sleep(Server.sleepTime); }
            catch (Exception e) { }
        }


    }

    private void handleGame() {
        moveBigTarget();
        moveSmallTarget();
        moveArrows();
    }


    private void moveBigTarget() {
        if (bigTargetMoveUp) {
            serverState.bigTargetY -= targetSpeed;
            if (serverState.bigTargetY < highTargetLimitY) bigTargetMoveUp = false;
        }
        else {
            serverState.bigTargetY += targetSpeed;
            if (serverState.bigTargetY > lowTargetLimitY) {
                bigTargetMoveUp = true;
            }
        }
    }
    private void moveSmallTarget() {
        if (smallTargetMoveUp) {
            serverState.smallTargetY -= targetSpeed * 1.5;
            if (serverState.smallTargetY < highTargetLimitY) smallTargetMoveUp = false;
        }
        else {
            serverState.smallTargetY += targetSpeed  * 1.5;
            if (serverState.smallTargetY > lowTargetLimitY) {
                smallTargetMoveUp = true;
            }
        }
    }
    private void moveArrows() {
        for (int i = 0; i < arrowsLaunched.size(); i++) {
            if (arrowsLaunched.get(i)) {
                Double newPosition = serverState.arrowsPositionX.get(i) + arrowSpeed;
                if (newPosition > arrowRightLimitX) {
                    arrowsLaunched.set(i, false);
                    newPosition = arrowStartPosX;
                }
                serverState.arrowsPositionX.set(i, newPosition);
            }
        }
    }


    private void updateServerState() {
        boolean allPlayersReady = true;
        if (clientStates.size() == 0) allPlayersReady = false;

        for (int i = 0; i < clientStates.size(); i++) {
            ClientState clientState = clientStates.get(i);
            if (clientState == null) continue;

            if (serverState.playerNames.size() == i) {
                serverState.playerNames.add(clientState.playerName);
                serverState.playersReady.add(clientState.isReady);
                serverState.playerScores.add(0);
                serverState.arrowsPositionX.add(arrowStartPosX);
                arrowsLaunched.add(false);
            }

            else  {
                serverState.playerNames.set(i, clientState.playerName);
                serverState.playersReady.set(i, clientState.isReady);
                if (clientState.shot) arrowsLaunched.set(i, true);
            }

            if (!clientState.isReady) allPlayersReady = false;
        }


        serverState.gameIsStarted = allPlayersReady;
        if (clientStates.size() > 0)
            System.out.println(clientStates.get(0).isReady);

    }




}
