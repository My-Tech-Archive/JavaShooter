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


    // Конструктор
    public ClientDialog(ClientState clientState, Socket clientSocket) throws IOException {
        this.clientState = clientState;

        dataToServer = new DataOutputStream(clientSocket.getOutputStream());
        dataFromServer = new DataInputStream(clientSocket.getInputStream());
    }


    // Каждый временной отрезок (5 милисек) отрабатывает функция run
    public void run() {
        try {
            // По сути, каждый кадр клиентский диалог ожидает приема данных от сервера и каждый кадр отправляет на сервер состояние клиента

            while (true) {
                // Ждем от сервера данных
                if (dataFromServer.available() > 0) {
                    // Опа, пришли, парсим их из джейсона
                    serverState = gson.fromJson(dataFromServer.readUTF(), ServerState.class);
                }

                // Постоянно отправляет состояние клиента на сервер
                dataToServer.flush(); // Очистка потока
                dataToServer.writeUTF(gson.toJson(clientState, ClientState.class));

                clientState.shot = false;

                try { Thread.sleep(Server.clientSleepTime); }
                catch (Exception e) { }
            }


        } catch (IOException e) { System.out.println(e);}

    }

}
