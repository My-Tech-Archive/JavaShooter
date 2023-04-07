package com.example.shooter.server;

import com.example.shooter.ClientState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static int sleepTime = 5;

    int port = 8001;
    ServerState serverState = new ServerState();
    ArrayList<ServerDialog> dialogs = new ArrayList<>();

    double targetSpeed = 1;

    int highTargetLimitY = -275;
    int lowTargetLimitY = 285;

    boolean bigTargetMoveUp = false;
    boolean smallTargetMoveUp = false;



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
                    var serverDialog = new ServerDialog(serverState, clientSocket);
                    serverDialog.start();
                    dialogs.add(serverDialog);
                    System.out.println("Client connected");
                }
                catch (IOException e) { }
            }

        }).start();

        while (true) {
            updateServerState();
            if (serverState.gameIsStarted) handleGame();

            try { Thread.sleep(Server.sleepTime); }
            catch (Exception e) { }
        }


    }

    private void handleGame() {
        moveBigTarget();
        moveSmallTarget();
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


    private void updateServerState() {
        boolean allPlayersReady = true;
        if (dialogs.size() == 0) allPlayersReady = false;

        for (int i = 0; i < dialogs.size(); i++) {
            ClientState clientState = dialogs.get(i).clientState;
            if (clientState == null) continue;

            if (serverState.playerNames.size() == i) {
                serverState.playerNames.add(clientState.playerName);
                serverState.playersReady.add(clientState.isReady);
            }

            else  {
                serverState.playerNames.set(i, clientState.playerName);
                serverState.playersReady.set(i, clientState.isReady);
            }

            if (!clientState.isReady) allPlayersReady = false;
        }


        serverState.gameIsStarted = allPlayersReady;

    }




}
