package com.example.shooter;

import com.example.shooter.server.ClientMessage;
import com.example.shooter.server.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {

    int port = 8000;
    Connection connection;
    ClientThread gameThread;
    Controller controller;

    public ClientMessage clientMessage = new ClientMessage();


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        controller = fxmlLoader.getController();
        controller.client = this;

        stage.setTitle("Shooter");
        stage.setScene(scene);
        stage.show();
    }


    public boolean connectToServer(String playerName) throws IOException {
        try {
            InetAddress address = InetAddress.getLocalHost();
            Socket socket = new Socket(address, port);
            connection = new Connection(socket);
            clientMessage.playerName = playerName;
            connection.sendToServer(clientMessage);
            System.out.println("Send connect");

            gameThread = new ClientThread(this);
            gameThread.start();

            return true;
        }
        catch (IOException e) { return false; }
    }

    public void playerReady() throws IOException {
        clientMessage.isReady = true;
        connection.sendToServer(clientMessage);

    }






}