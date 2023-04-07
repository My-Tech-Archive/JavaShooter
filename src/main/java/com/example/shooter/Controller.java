package com.example.shooter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.UnknownHostException;

public class Controller {

    public Client client;
    @FXML
    private AnchorPane connectPanel;
    @FXML
    private TextField nameField;
    @FXML
    public Label playerName1;
    @FXML
    public Label playerName2;
    @FXML
    public Label playerName3;
    @FXML
    public Label playerName4;
    @FXML
    public AnchorPane playerPanel1;
    @FXML
    public AnchorPane playerPanel2;
    @FXML
    public AnchorPane playerPanel3;
    @FXML
    public AnchorPane playerPanel4;
    @FXML
    public Label score1;
    @FXML
    public Label score2;
    @FXML
    public Label score3;
    @FXML
    public Label score4;
    @FXML
    public Label shotCounter1;
    @FXML
    public Label shotCounter2;
    @FXML
    public Label shotCounter3;
    @FXML
    public Label shotCounter4;




    @FXML
    void onButtonConnectToServerPressed(ActionEvent event) throws IOException {
        String playerName = nameField.getText();
        if (client.connectToServer(playerName)) {
            connectPanel.setVisible(false);
        }

    }


    @FXML
    void onButtonReadyPressed(ActionEvent event) throws IOException {
        client.playerReady();
    }

    @FXML
    void onButtonShootPressed(ActionEvent event) {

    }

    @FXML
    void onButtonStopPressed(ActionEvent event) {

    }






}