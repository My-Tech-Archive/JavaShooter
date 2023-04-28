package com.example.shooter.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectStream {

    public DataOutputStream dataToClient;
    public DataInputStream dataFromClient;

    public ConnectStream(Socket clientSocket) throws IOException {
        dataToClient = new DataOutputStream(clientSocket.getOutputStream());
        dataFromClient = new DataInputStream(clientSocket.getInputStream());
    }



}
