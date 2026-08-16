package com.gruppo10.controller;

import com.gruppo10.ServerContext;
import com.gruppo10.ServerPublisher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
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

    /**
     * Avvia il server RMI e pubblica i service disponibili.
     */
    @FXML
    public void avviaServer() {
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
