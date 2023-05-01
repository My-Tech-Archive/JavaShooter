package com.example.shooter.server;

import com.example.shooter.ClientState;
import com.example.shooter.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.hibernate.Session;

public class Server {

    public static int sleepTime = 5;
    public static int clientSleepTime = 5;

    int port = 8001;
    public ServerState serverState = new ServerState();
    public ArrayList<ClientState> clientStates = new ArrayList<>();
    ServerDialog serverDialog;

    double targetSpeed = 1;
    double arrowSpeed = 7;
    double arrowRightLimitX = 850;
    double arrowStartPosX = 40;

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
        //Session session = HibernateUtils
    }

    private void startServer() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");

        // Отдельный поток для подключений
        new Thread(()->{
            while (true) {
                try {
                    // accept ждет подключения нового клиента.
                    Socket clientSocket = serverSocket.accept();
                    // Создаем подключение игрока
                    // И, если еще не было создано диалога на стороне сервера, создаем его. Он будет единственным, который будет обслуживать всех клиентов
                    Connection connection = new Connection(clientSocket);
                    if (serverDialog == null) {
                        serverDialog = new ServerDialog(this);
                        serverDialog.start();
                    }
                    // Добавляем в диалог новое подключение, чтобы он понимал, кто к нему подключен
                    // И мог отправлять подключенным клиентам данные
                    serverDialog.connections.add(connection);
                    System.out.println("Client connected");
                }
                catch (IOException e) { }
            }

        }).start();

        resetGame();

        // Собственный поток для обновления состояния игры
        while (true) {
            // Обновление данных сервера на основе полученной модели от клиента
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
        handleCollisions();
    }


    private void handleCollisions() {
        for (int i = 0; i < serverState.arrowsPositionX.size(); i++) {
            double arrowPosX = serverState.arrowsPositionX.get(i);
            boolean collidedToBig = Tools.isCollided(arrowPosX + 70, arrowsPositionY[i],
                    bigTargetPosX, serverState.bigTargetY, bigTargetRadius);
            boolean collidedToSmall = Tools.isCollided(arrowPosX + 70, arrowsPositionY[i],
                    smallTargetPosX, serverState.smallTargetY, smallTargetRadius);

            if (collidedToBig) {
                int newScore = serverState.playerScores.get(i) + 1;
                serverState.playerScores.set(i, newScore);

                if (newScore >= winScore) serverState.winner = serverState.playerNames.get(i);

                serverState.arrowsPositionX.set(i, arrowStartPosX);
                arrowsLaunched.set(i, false);
                System.out.println("Big");
            }
            else if (collidedToSmall) {
                int newScore = serverState.playerScores.get(i) + 2;
                serverState.playerScores.set(i, newScore);

                if (newScore >= winScore) serverState.winner = serverState.playerNames.get(i);

                serverState.arrowsPositionX.set(i, arrowStartPosX);
                arrowsLaunched.set(i, false);
                System.out.println("Small");
            }
        }
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

    private void resetGame() {
        serverState.smallTargetY = (highTargetLimitY + lowTargetLimitY)/2;
        serverState.bigTargetY = (highTargetLimitY + lowTargetLimitY)/2;
        serverState.winner = "";
        for (int i = 0; i < serverState.playerScores.size(); i++) {
            serverState.playerScores.set(i, 0);
            serverState.playerShots.set(i, 0);
        }
    }


    private void updateServerState() {
        boolean allPlayersReady = true;
        if (clientStates.size() == 0) allPlayersReady = false;

        // Для каждого игрока
        for (int i = 0; i < clientStates.size(); i++) {
            // В зависимости от количества игроков
            ClientState clientState = clientStates.get(i);
            if (clientState == null) continue;

            if (serverState.playerNames.size() == i) {

                // Заполнение листов данными от игроков
                if (serverState.playerNames.contains(clientState.playerName))
                    serverState.playerNames.add(clientState.playerName + "1");
                else serverState.playerNames.add(clientState.playerName);

                serverState.playersReady.add(clientState.isReady);
                serverState.playerScores.add(0);
                serverState.playerShots.add(0);
                serverState.arrowsPositionX.add(arrowStartPosX);
                arrowsLaunched.add(false);
            }

            else  {

                serverState.playersReady.set(i, clientState.isReady);
                if (clientState.shot) {
                    if (!arrowsLaunched.get(i)){
                        int newShots = serverState.playerShots.get(i) + 1;
                        serverState.playerShots.set(i, newShots);
                    }
                    arrowsLaunched.set(i, true);
                }
            }

            if (!clientState.isReady) allPlayersReady = false;
        }

        if (!allPlayersReady && serverState.winner.length() > 0) resetGame();

        serverState.gameIsStarted = allPlayersReady;
    }




}
