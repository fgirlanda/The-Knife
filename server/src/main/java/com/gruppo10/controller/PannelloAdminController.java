package com.gruppo10.controller;

import com.gruppo10.ServerContext;
import com.gruppo10.ServerPublisher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller dell'interfaccia di amministrazione del server.
 * Permette di configurare la connessione al database e di avviare la
 * pubblicazione dei service RMI.
 */
public class PannelloAdminController {
    /** Finestra principale associata all'interfaccia. */
    private Stage stage;

    /** Contesto del server con i componenti condivisi. */
    ServerContext serverContext;

    /** Publisher dei service remoti del server. */
    ServerPublisher serverPublisher;

    /** Campo per l'host del database. */
    @FXML
    private TextField hostField;

    /** Campo per il nome del database. */
    @FXML
    private TextField dbField;

    /** Campo per la porta del database. */
    @FXML
    private TextField portaField;

    /** Campo per l'username di accesso al database. */
    @FXML
    private TextField usernameField;

    /** Campo per la password di accesso al database. */
    @FXML
    private TextField passwordField;

    /** Pulsante per avviare la connessione al database. */
    @FXML
    private Button btnConnetti;

    /** Pulsante per avviare il server RMI. */
    @FXML
    private Button btnAvvia;

    /**
     * Imposta lo stage associato alla vista.
     *
     * @param stage finestra principale del pannello di amministrazione
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Disabilita o abilita un bottone della GUI.
     *
     * @param bottone pulsante da aggiornare
     * @param abilita {@code true} per disabilitare, {@code false} per abilitare
     */
    public void disabilitaBottone(Button bottone, boolean abilita) {
        bottone.setDisable(abilita);
    }

    /**
     * Imposta il contesto del server.
     *
     * @param serverContext contesto condiviso del server
     */
    public void setServerContext(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    /**
     * Imposta il publisher dei service remoti.
     *
     * @param serverPublisher oggetto incaricato della registrazione dei service
     */
    public void setServerPublisher(ServerPublisher serverPublisher) {
        this.serverPublisher = serverPublisher;
    }

    /**
     * Connette il server al database PostgreSQL usando i valori immessi nella GUI.
     */
    @FXML
    public void connettiDB() {
        String host = hostField.getText();
        int porta = Integer.parseInt(portaField.getText());
        String database = dbField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        serverContext.getManagerDB().connetti(host, porta, database, username, password);
    }

    /**
     * Avvia il server RMI e pubblica i service disponibili.
     */
    @FXML
    public void avviaServer() {
        try {
            serverPublisher.avvia();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
