package com.example.shooter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    int port = 8000;
    ServerSocket serverSocket;
    ArrayList<Connection> connections = new ArrayList<Connection>();

    ServerMessage serverMessage = new ServerMessage();


    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }

    private void startServer() throws IOException {
        serverSocket = new ServerSocket(port, 0);
        System.out.println("Server started");

        new ServerThread(this).start();






    }





}
