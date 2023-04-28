package com.example.shooter.server;

import com.example.shooter.ClientRequests;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class ServerChannel extends Thread {

    Server server;
    public ArrayList<Connection> connections = new ArrayList<>();
    Gson gson = new Gson();


    public ServerChannel(Server server) throws IOException {
        this.server = server;
    }


    public void run() {
        try {
            while (true) {
                // От каждого клиента постоянно принимаем его состояние
                for (int i = 0; i < connections.size(); i++) {
                    if (connections.get(i).fromClient.available() > 0) {
                        var clientState = gson.fromJson(connections.get(i).fromClient.readUTF(), ClientRequests.class);

                        if (server.clientRequests.size() == i) server.clientRequests.add(clientState);
                        else server.clientRequests.set(i, clientState);
                    }
                }

                Thread.sleep(Server.sleepTime);
            }
        }

        catch (IOException e) {} catch (InterruptedException e) {}
    }

    public void send() throws IOException {
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).forClient.flush();
            connections.get(i).forClient.writeUTF(gson.toJson(server.serverModel));
        }
    }


}
