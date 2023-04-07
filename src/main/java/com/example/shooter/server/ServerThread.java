package com.example.shooter.server;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread{

    Server server;


    public ServerThread(Server server) { this.server = server; }




    private void connectClients() {

        while (server.connections.size() < 4) {
            try (Socket clientSocket = server.serverSocket.accept()) {
                var connection = new Connection(clientSocket);
                server.connections.add(connection);
                System.out.println("Client connected");

                ClientMessage clientMessage = connection.acceptFromClient();
                if (clientMessage == null) continue;

                server.serverMessage.playerNames.add(clientMessage.playerName);
                server.serverMessage.playersReady.add(false);
                sendDataToClients();
            }
            catch (IOException e) { }
        }
    }


    private void sendDataToClients() throws IOException {
        for (var connection: server.connections) {
            connection.sendToClient(server.serverMessage);
        }
    }


    private void acceptDataFromClients() throws IOException {
        while (true) {
            for (int i = 0; i < server.connections.size(); i++) {
                ClientMessage clientMessage = server.connections.get(i).acceptFromClient();
                if (clientMessage == null) continue;

                server.serverMessage.playersReady.set(i, clientMessage.isReady);
            }
        }

    }





    public void run() {
        try {
            connectClients();
            new Thread(()-> { try { acceptDataFromClients();} catch (IOException e) { } }).start();
            while(true) { sendDataToClients(); System.out.println("Sended"); }

        }
        catch (IOException e) {

            throw new RuntimeException(e);
        }

    }
}
