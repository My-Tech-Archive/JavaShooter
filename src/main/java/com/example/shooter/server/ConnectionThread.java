package com.example.shooter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionThread extends Thread  {


    Server server;

    public ConnectionThread(Server server) {
        this.server = server;
    }


    public void run() {

    }

}
