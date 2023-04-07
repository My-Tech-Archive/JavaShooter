package com.example.shooter.server;

import com.example.shooter.ClientState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

    public DataOutputStream dataToClient;
    public DataInputStream dataFromClient;

    public Connection(Socket clientSocket) throws IOException {
        dataToClient = new DataOutputStream(clientSocket.getOutputStream());
        dataFromClient = new DataInputStream(clientSocket.getInputStream());
    }



}
