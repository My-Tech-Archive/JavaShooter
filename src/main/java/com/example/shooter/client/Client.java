package com.example.shooter.client;

import com.example.shooter.server.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {

    int port = 8001;
    Controller controller;

    public ClientState clientState = new ClientState();
    ClientDialog dialog;

    public static void main() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Загрузка интерфейса
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("/window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        controller = fxmlLoader.getController();
        controller.client = this;

        stage.setTitle("Shooter");
        stage.setScene(scene);
        stage.show();


    }


    public void connectToServer(String playerName) throws IOException {
        try {
            InetAddress address = InetAddress.getLocalHost();
            // Создаем сокет
            Socket socket = new Socket(address, port);

            dialog = new ClientDialog(clientState, socket);
            dialog.start();

            clientState.playerName = playerName;
            clientState.isReady = false;
            System.out.println("Connect request");



            // Создание нового потока с функцией обновления интерфейса согласно данным с сервера
            new Thread(()-> {
                while (true) {
                    if (dialog != null) updateInterface();
                    try { Thread.sleep(Server.sleepTime); }
                    catch (Exception e) { }
                }
            }).start();
        }
        catch (IOException e) { }
    }



    public void updateInterface() {
        // Клиентский диалог принимает состояние сервера. Оттуда берем данные и используем их для обновления состояния интерфейса
        if (dialog.serverState == null) return;

        // Platform.runLater разрешает изменение интерфейса вне основного потока
        Platform.runLater(()->{
            updatePlayerNames(dialog.serverState);
            updateReady(dialog.serverState);

            updateWins(dialog.serverState);
            updateLeaderPanel(dialog.serverState);

            if (dialog.serverState.gameIsStarted) {
                updateTargets(dialog.serverState);
                updateArrows(dialog.serverState);
                updateScores(dialog.serverState);
                updateShotCounters(dialog.serverState);
                updateWinWindow(dialog.serverState);

            }

        });
    }






    private void updateTargets(ServerState gameState) {
        controller.bigTarget.setLayoutY(gameState.bigTargetY);
        controller.smallTarget.setLayoutY(gameState.smallTargetY);
    }


    private void updatePlayerNames(ServerState gameState) {
        for (int i = 0; i < gameState.playerNames.size(); i++) {



            if (i == 0) {
                controller.playerPanel1.setVisible(true);
                controller.playerName1.setText(gameState.playerNames.get(i));
            }
            else if (i == 1) {
                controller.playerPanel2.setVisible(true);
                controller.playerName2.setText(gameState.playerNames.get(i));
            }
            else if (i == 2) {
                controller.playerPanel3.setVisible(true);
                controller.playerName3.setText(gameState.playerNames.get(i));
            }
            else if (i == 3) {
                controller.playerPanel4.setVisible(true);
                controller.playerName4.setText(gameState.playerNames.get(i));
            }
        }
    }

    private void updateReady(ServerState serverMessage) {
        for (int i = 0; i < serverMessage.playersReady.size(); i++) {
            if (i == 0) {
                if (serverMessage.playersReady.get(i)) {
                    controller.score1.setVisible(true);
                    controller.score1.setText("Ready");
                }
                else {
                    controller.score1.setVisible(false);
                    controller.shotCounter1.setVisible(false);
                }
            }
            else if (i == 1) {
                if (serverMessage.playersReady.get(i)) {
                    controller.score2.setVisible(true);
                    controller.score2.setText("Ready");
                }
                else {
                    controller.score2.setVisible(false);
                    controller.shotCounter2.setVisible(false);
                }
            }
            else if (i == 2) {
                if (serverMessage.playersReady.get(i)) {
                    controller.score3.setVisible(true);
                    controller.score3.setText("Ready");
                }
                else {
                    controller.score3.setVisible(false);
                    controller.shotCounter3.setVisible(false);
                }
            }
            else if (i == 3) {
                if (serverMessage.playersReady.get(i)) {
                    controller.score4.setVisible(true);
                    controller.score4.setText("Ready");
                }
                else {
                    controller.score4.setVisible(false);
                    controller.shotCounter4.setVisible(false);
                }
            }
        }
    }

    private void updateArrows(ServerState serverMessage) {
        for (int i = 0; i < serverMessage.arrowsPositionX.size(); i++) {
            if (i == 0) controller.arrow1.setLayoutX(serverMessage.arrowsPositionX.get(i));
            if (i == 1) controller.arrow2.setLayoutX(serverMessage.arrowsPositionX.get(i));
            if (i == 2) controller.arrow3.setLayoutX(serverMessage.arrowsPositionX.get(i));
            if (i == 3) controller.arrow4.setLayoutX(serverMessage.arrowsPositionX.get(i));
        }
    }

    private void updateScores(ServerState serverMessage) {
        for (int i = 0; i < serverMessage.playerScores.size(); i++) {
            if (i == 0) controller.score1.setText("Score: " + serverMessage.playerScores.get(i).toString());
            if (i == 1) controller.score2.setText("Score: " + serverMessage.playerScores.get(i).toString());
            if (i == 2) controller.score3.setText("Score: " + serverMessage.playerScores.get(i).toString());
            if (i == 3) controller.score4.setText("Score: " + serverMessage.playerScores.get(i).toString());
        }
    }

    private void updateWins(ServerState serverMessage) {
        for (int i = 0; i < serverMessage.victories.size(); i++) {
            if (i == 0) controller.victories1.setText("Victories: " + serverMessage.victories.get(i).toString());
            if (i == 1) controller.victories2.setText("Victories: " + serverMessage.victories.get(i).toString());
            if (i == 2) controller.victories3.setText("Victories: " + serverMessage.victories.get(i).toString());
            if (i == 3) controller.victories4.setText("Victories: " + serverMessage.victories.get(i).toString());
        }
    }

    private void updateShotCounters(ServerState serverMessage) {
        for (int i = 0; i < serverMessage.playerShots.size(); i++) {
            if (i == 0) {
                controller.shotCounter1.setVisible(true);
                controller.shotCounter1.setText("Shots: " + serverMessage.playerShots.get(i).toString());
            }
            if (i == 1)  {
                controller.shotCounter2.setVisible(true);
                controller.shotCounter2.setText("Shots: " + serverMessage.playerShots.get(i).toString());
            }
            if (i == 2) {
                controller.shotCounter3.setVisible(true);
                controller.shotCounter3.setText("Shots: " + serverMessage.playerShots.get(i).toString());
            }
            if (i == 3) {
                controller.shotCounter4.setVisible(true);
                controller.shotCounter4.setText("Shots: " + serverMessage.playerShots.get(i).toString());
            }
        }
    }

    private void updateWinWindow(ServerState serverMessage) {
        if (serverMessage.winner.length() > 0) {
            controller.winnerPanel.setVisible(true);
            controller.winnerText.setText("Winner: " + serverMessage.winner);
            clientState.isReady = false;
        }
    }

    private void updateLeaderPanel(ServerState serverMessage) {
        for (int i = 0; i < serverMessage.leaders.size() && i < 10; i++) {

            String playerName = serverMessage.leaders.get(i).getName();
            int wins = serverMessage.leaders.get(i).getWins();

            if (i == 0) {
                controller.top1.setVisible(true);
                controller.top1.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 1) {
                controller.top2.setVisible(true);
                controller.top2.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 2) {
                controller.top3.setVisible(true);
                controller.top3.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 3) {
                controller.top4.setVisible(true);
                controller.top4.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 4) {
                controller.top5.setVisible(true);
                controller.top5.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 5) {
                controller.top6.setVisible(true);
                controller.top6.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 6) {
                controller.top7.setVisible(true);
                controller.top7.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 7) {
                controller.top8.setVisible(true);
                controller.top8.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 8) {
                controller.top9.setVisible(true);
                controller.top9.setText((i+1) + ") " + playerName + ": " + wins);
            }
            else if (i == 9) {
                controller.top10.setVisible(true);
                controller.top10.setText((i+1) + ") " + playerName + ": " + wins);
            }




            
        }
        
    }



}