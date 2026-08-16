package com.gruppo10.controller;

import com.gruppo10.ServerContext;
import com.gruppo10.ServerPublisher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
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

    @FXML
    private Label databaseStatusLabel;

    @FXML
    private Label databaseStatusDetail;

    @FXML
    private Label databaseStatusIcon;

    @FXML
    private Circle databaseStatusDot;

    @FXML
    private Label serverStatusLabel;

    @FXML
    private Circle serverStatusDot;

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
        try {
            String host = hostField.getText();
            int porta = Integer.parseInt(portaField.getText());
            String database = dbField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            serverContext.getManagerDB().connetti(host, porta, database, username, password);
            aggiornaStatoDatabase(true, "Database configurato",
                    "Parametri di connessione acquisiti con successo");
        } catch (NumberFormatException e) {
            aggiornaStatoDatabase(false, "Porta non valida",
                    "Inserisci un numero valido nel campo Porta");
        } catch (RuntimeException e) {
            aggiornaStatoDatabase(false, "Configurazione non riuscita", e.getMessage());
        }
    }

    @FXML
    public void avviaServer(){
        ServerPublisher serverPublisher = new ServerPublisher(serverContext);
        try {
            serverPublisher.avvia();
            aggiornaStatoServer(true, "Server avviato sulla porta RMI 1099");
            btnAvvia.setDisable(true);
            btnAvvia.setText("✓  Server Avviato");
        } catch (Exception e) {
            aggiornaStatoServer(false, "Avvio non riuscito: " + messaggioErrore(e));
        }
    }

    private void aggiornaStatoDatabase(boolean successo, String titolo, String dettaglio) {
        databaseStatusLabel.setText(titolo);
        databaseStatusDetail.setText(dettaglio == null ? "" : dettaglio);
        databaseStatusIcon.setText(successo ? "✓" : "!");
        applicaClasseStato(databaseStatusDot, successo);
        applicaClasseTesto(databaseStatusLabel, successo);
        databaseStatusIcon.getStyleClass().removeAll("status-icon-success", "status-icon-error");
        databaseStatusIcon.getStyleClass().add(successo ? "status-icon-success" : "status-icon-error");
    }

    private void aggiornaStatoServer(boolean successo, String messaggio) {
        serverStatusLabel.setText(messaggio);
        applicaClasseStato(serverStatusDot, successo);
        applicaClasseTesto(serverStatusLabel, successo);
    }

    private void applicaClasseStato(Circle indicatore, boolean successo) {
        indicatore.getStyleClass().removeAll(
                "status-dot-idle", "status-dot-success", "status-dot-error");
        indicatore.getStyleClass().add(successo ? "status-dot-success" : "status-dot-error");
    }

    private void applicaClasseTesto(Label label, boolean successo) {
        label.getStyleClass().removeAll("status-success", "status-error");
        label.getStyleClass().add(successo ? "status-success" : "status-error");
    }

    private String messaggioErrore(Exception errore) {
        return errore.getMessage() == null ? errore.getClass().getSimpleName() : errore.getMessage();
    }
}
