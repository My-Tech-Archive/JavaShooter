package com.example.shooter.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;

public class Connection {
    Socket clientSocket;
    OutputStream outputStream;
    DataOutputStream dataOutputStream;
    InputStream inputStream;
    DataInputStream dataInputStream;

    Gson gson = new Gson();






    public Connection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        outputStream = clientSocket.getOutputStream();
        inputStream = clientSocket.getInputStream();
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);
    }


    public ServerMessage acceptFromServer() throws IOException {
        try {
            inputStream.read(new byte[1]);
            inputStream.reset();
            return gson.fromJson(dataInputStream.readUTF(), ServerMessage.class);
        }
       catch (IOException e) { return null; }
    }
    public void sendToServer(ClientMessage message) throws IOException {
        dataOutputStream.writeUTF(gson.toJson(message, ClientMessage.class));
    }

    public ClientMessage acceptFromClient() throws IOException {
        try {
            inputStream.read(new byte[1]);
            inputStream.reset();
            return gson.fromJson(dataInputStream.readUTF(), ClientMessage.class);
        }
        catch (IOException e) { return null; }
    }

    public void sendToClient(ServerMessage message) throws IOException {
       dataOutputStream.writeUTF(gson.toJson(message, ServerMessage.class));
    }


}
