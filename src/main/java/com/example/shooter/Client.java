package com.example.shooter;

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


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("window.fxml"));
        Scene scene = new Scene(fxmlLoader.
                load(), 1200, 800);
        controller = fxmlLoader.getController();
        controller.client = this;

        stage.setTitle("Shooter");
        stage.setScene(scene);
        stage.show();


    }


    public void connectToServer(String playerName) throws IOException {
        try {
            InetAddress address = InetAddress.getLocalHost();
            Socket socket = new Socket(address, port);
            dialog = new ClientDialog(clientState, socket);
            dialog.start();
            clientState.playerName = playerName;
            clientState.isReady = false;
            System.out.println("Connect request");

            new Thread(()-> {
                while (true) {
                    if (dialog != null) updateState();
                    try { Thread.sleep(Server.sleepTime); }
                    catch (Exception e) { }
                }
            }).start();
        }
        catch (IOException e) { }
    }



    public void updateState() {
        if (dialog.serverState == null) return;

        Platform.runLater(()->{
            updatePlayerNames(dialog.serverState);
            updateReady(dialog.serverState);

            if (dialog.serverState.gameIsStarted) {
                updateTargets(dialog.serverState);
                updateArrows(dialog.serverState);
            }

        });
    }






    private void updateTargets(ServerState gameState) {
        controller.bigTarget.setLayoutY(gameState.bigTargetY);
        controller.smallTarget.setLayoutY(gameState.smallTargetY);
        System.out.println(controller.bigTarget.getCenterX());
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
            if (i == 0 && serverMessage.playersReady.get(i)) {
                controller.score1.setVisible(true);
                controller.score1.setText("Ready");
            }
            else if (i == 1 && serverMessage.playersReady.get(i)) {
                controller.score2.setVisible(true);
                controller.score2.setText("Ready");
            }
            else if (i == 2 && serverMessage.playersReady.get(i)) {
                controller.score3.setVisible(true);
                controller.score3.setText("Ready");
            }
            else if (i == 3 && serverMessage.playersReady.get(i)) {
                controller.score4.setVisible(true);
                controller.score4.setText("Ready");
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



}