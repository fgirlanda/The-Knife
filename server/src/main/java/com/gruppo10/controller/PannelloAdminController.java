package com.gruppo10.controller;

import com.gruppo10.ServerContext;
import com.gruppo10.ServerPublisher;

import javafx.concurrent.Task;
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
        // 1. Controllo base: verifica che i campi non siano vuoti
        if (hostField.getText().isBlank() || portaField.getText().isBlank() || 
            dbField.getText().isBlank() || usernameField.getText().isBlank()) {
            aggiornaStatoDatabase(false, "Dati mancanti", "Compila tutti i campi obbligatori.");
            return;
        }

        // Estrazione delle variabili (devono essere 'effectively final' per il Task)
        final String host = hostField.getText();
        final String database = dbField.getText();
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final int porta;

        try {
            porta = Integer.parseInt(portaField.getText());
        } catch (NumberFormatException e) {
            aggiornaStatoDatabase(false, "Porta non valida", "Inserisci un numero valido nel campo Porta");
            return;
        }

        // 2. Creazione del Task per l'operazione in background
        Task<Void> taskConnessione = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Questa riga viene eseguita in background e NON blocca la UI
                serverContext.getManagerDB().connetti(host, porta, database, username, password);
                return null;
            }
        };

        // Cosa fare in caso di SUCCESSO (viene eseguito automaticamente sul thread della UI)
        taskConnessione.setOnSucceeded(e -> {
            aggiornaStatoDatabase(true, "Database configurato", "Parametri di connessione acquisiti con successo");
        });

        // Cosa fare in caso di FALLIMENTO (viene eseguito automaticamente sul thread della UI)
        taskConnessione.setOnFailed(e -> {
            Throwable eccezione = taskConnessione.getException();
            // Qui potresti controllare se l'eccezione è di tipo SQLException
            aggiornaStatoDatabase(false, "Configurazione non riuscita", eccezione.getMessage());
        });

        // Aggiornamento UI opzionale mentre carica (es. disabilitare il bottone "Connetti")
        // bottoneConnetti.setDisable(true); // Se hai il riferimento al bottone

        // 3. Avvio effettivo del thread
        new Thread(taskConnessione).start();
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
