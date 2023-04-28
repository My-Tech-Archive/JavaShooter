package com.example.shooter;

import com.example.shooter.server.Server;
import com.example.shooter.server.ServerModel;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientChannel extends Thread {

    ClientRequests clientRequests;
    public ServerModel serverModel;
    DataOutputStream forServer;
    DataInputStream fromServer;
    Gson gson = new Gson();


    // Конструктор
    public ClientChannel(ClientRequests clientRequests, Socket clientSocket) throws IOException {
        this.clientRequests = clientRequests;

        forServer = new DataOutputStream(clientSocket.getOutputStream());
        fromServer = new DataInputStream(clientSocket.getInputStream());
    }


    public void run() {
        try {

            while (true) {
                // Ждем от сервера данных
                if (fromServer.available() > 0) {
                    serverModel = gson.fromJson(fromServer.readUTF(), ServerModel.class);
                }

                forServer.flush(); // Очистка потока
                forServer.writeUTF(gson.toJson(clientRequests, ClientRequests.class));

                clientRequests.shot = false;

                try { Thread.sleep(Server.clientSleepTime); }
                catch (Exception e) { }
            }


        } catch (IOException e) { System.out.println(e);}

    }

}
