package com.example.shooter.server;

import com.example.shooter.ClientState;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerDialog extends Thread {

    ServerState serverState;
    public ClientState clientState;
    DataOutputStream dataToClient;
    DataInputStream dataFromClient;
    Gson gson = new Gson();


    public ServerDialog(ServerState serverState, Socket clientSocket) throws IOException {
        this.serverState = serverState;
        dataToClient = new DataOutputStream(clientSocket.getOutputStream());
        dataFromClient = new DataInputStream(clientSocket.getInputStream());
    }


    public void run() {
        try {
            while (true) {
                if (dataFromClient.available() > 0) {
                    clientState = gson.fromJson(dataFromClient.readUTF(), ClientState.class);
                }

                dataToClient.writeUTF(gson.toJson(serverState));
                Thread.sleep(Server.sleepTime);
            }
        }

        catch (IOException e) {} catch (InterruptedException e) {}
    }

}
