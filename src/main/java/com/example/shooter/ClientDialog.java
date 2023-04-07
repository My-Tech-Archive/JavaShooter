package com.example.shooter;

import com.example.shooter.server.Server;
import com.example.shooter.server.ServerState;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientDialog extends Thread {

    ClientState clientState;
    public ServerState serverState;
    DataOutputStream dataToServer;
    DataInputStream dataFromServer;
    Gson gson = new Gson();


    public ClientDialog(ClientState clientState, Socket clientSocket) throws IOException {
        this.clientState = clientState;
        dataToServer = new DataOutputStream(clientSocket.getOutputStream());
        dataFromServer = new DataInputStream(clientSocket.getInputStream());
    }


    public void run() {
        try {
            while (true) {
                if (dataFromServer.available() > 0) {
                    serverState = gson.fromJson(dataFromServer.readUTF(), ServerState.class);
                }

                dataToServer.flush();
                dataToServer.writeUTF(gson.toJson(clientState, ClientState.class));

                clientState.shot = false;

                try { Thread.sleep(Server.sleepTime); }
                catch (Exception e) { }
            }


        } catch (IOException e) { System.out.println(e);}

    }

}
