package com.example.shooter.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

    public DataOutputStream forClient;
    public DataInputStream fromClient;

    public Connection(Socket clientSocket) throws IOException {
        forClient = new DataOutputStream(clientSocket.getOutputStream());
        fromClient = new DataInputStream(clientSocket.getInputStream());
    }



}
