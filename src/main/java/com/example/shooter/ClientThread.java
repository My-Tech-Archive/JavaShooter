package com.example.shooter;


import com.example.shooter.server.ClientMessage;
import com.example.shooter.server.Connection;
import com.example.shooter.server.ServerMessage;
import javafx.application.Platform;

import java.io.IOException;

public class ClientThread extends Thread {

    Client client;

    public ClientThread(Client client) {
        this.client = client;
    }


    public void run() {
        while(true) {

            try {
                System.out.println("Thread");
                client.connection.sendToServer(client.clientMessage);
                System.out.println("Wait");

                ServerMessage serverMessage = client.connection.acceptFromServer();
                if (serverMessage == null) continue;

                System.out.println(serverMessage.playerNames.size());
                Platform.runLater(()->{
                    updatePlayerNames(serverMessage);
                    updateReady(serverMessage);


                });

            }
            catch (IOException e) {
                System.out.println("Error");
            }
        }
    }


    private void updatePlayerNames(ServerMessage serverMessage) {
        for (int i = 0; i < serverMessage.playerNames.size(); i++) {
            if (i == 0) {
                client.controller.playerPanel1.setVisible(true);
                client.controller.playerName1.setText(serverMessage.playerNames.get(i));
            }
            else if (i == 1) {
                client.controller.playerPanel2.setVisible(true);
                client.controller.playerName2.setText(serverMessage.playerNames.get(i));
            }
            else if (i == 2) {
                client.controller.playerPanel3.setVisible(true);
                client.controller.playerName3.setText(serverMessage.playerNames.get(i));
            }
            else if (i == 3) {
                client.controller.playerPanel4.setVisible(true);
                client.controller.playerName4.setText(serverMessage.playerNames.get(i));
            }
        }
    }


    private void updateReady(ServerMessage serverMessage) {
        for (int i = 0; i < serverMessage.playersReady.size(); i++) {
            if (i == 0 && serverMessage.playersReady.get(i)) {
                client.controller.score1.setVisible(true);
                client.controller.score1.setText("Ready");
            }
            else if (i == 1 && serverMessage.playersReady.get(i)) {
                client.controller.score2.setVisible(true);
                client.controller.score2.setText("Ready");
            }
            else if (i == 2 && serverMessage.playersReady.get(i)) {
                client.controller.score3.setVisible(true);
                client.controller.score3.setText("Ready");
            }
            else if (i == 3 && serverMessage.playersReady.get(i)) {
                client.controller.score4.setVisible(true);
                client.controller.score4.setText("Ready");
            }
        }
    }








}
