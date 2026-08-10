package com.gruppo10.controller;

import com.gruppo10.ServerContext;
import com.gruppo10.ServerPublisher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PannelloAdminController {
    private Stage stage;

    ServerContext serverContext;

    @FXML
    private TextField hostField;

    @FXML
    private TextField dbField;

    @FXML
    private TextField portaField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button btnConnetti;
    
    @FXML
    private Button btnAvvia;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void disabilitaBottone(Button bottone, boolean abilita){
        bottone.setDisable(abilita);
    }

    public void setServerContext(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @FXML
    public void connettiDB() {
        String host = hostField.getText();
        int porta = Integer.parseInt(portaField.getText());
        String database = dbField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        serverContext.getManagerDB().connetti(host, porta, database, username, password);
    }

    @FXML
    public void avviaServer(){
        ServerPublisher serverPublisher = new ServerPublisher(serverContext);
        try {
            serverPublisher.avvia();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
