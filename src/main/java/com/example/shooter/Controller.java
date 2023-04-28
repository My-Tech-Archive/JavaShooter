package com.example.shooter;

import com.example.shooter.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Controller {

    public Client client;

    @FXML
    public Circle bigTarget;
    @FXML
    public Circle smallTarget;
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
    public Circle arrow1;
    @FXML
    public Circle arrow2;
    @FXML
    public Circle arrow3;
    @FXML
    public Circle arrow4;
    @FXML
    public AnchorPane winnerPanel;
    @FXML
    public Label winnerText;



    @FXML
    void onButtonConnectToServerPressed(ActionEvent event) throws IOException {
        // Когда нажали на кнопку "подключиться" срабатывает эта функция
        String playerName = nameField.getText();
        client.connectToServer(playerName);
        connectPanel.setVisible(false);
    }


    @FXML
    void onButtonReadyPressed(ActionEvent event) throws IOException {
        client.clientState.isReady = true;
    }

    @FXML
    void onButtonShootPressed(ActionEvent event) {
        client.clientState.shot = true;

    }

    @FXML
    void onButtonStopPressed(ActionEvent event) {
        client.clientState.isReady = false;

    }

    @FXML
    void onButtonPlayAgainPressed(ActionEvent event) {
        winnerPanel.setVisible(false);
    }





}