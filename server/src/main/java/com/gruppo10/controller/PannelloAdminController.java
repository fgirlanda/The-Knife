package com.gruppo10.controller;

import com.gruppo10.ServerContext;
import com.gruppo10.ServerPublisher;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

/**
 * Controller dell'interfaccia di amministrazione del server.
 * Permette di configurare la connessione al database e di avviare la
 * pubblicazione dei service RMI.
 */
public class PannelloAdminController {
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
    private PasswordField passwordField;

    /** Pulsante per avviare la connessione al database. */
    @FXML
    private Button btnConnetti;

    /** Pulsante per avviare il server RMI. */
    @FXML
    private Button btnAvvia;

    /** Etichetta che mostra lo stato della connessione al database. */
    @FXML
    private Label databaseStatusLabel;

    /** Dettaglio testuale dell'ultimo tentativo di connessione al database. */
    @FXML
    private Label databaseStatusDetail;

    /** Icona testuale associata allo stato del database. */
    @FXML
    private Label databaseStatusIcon;

    /** Indicatore circolare dello stato del database. */
    @FXML
    private Circle databaseStatusDot;

    /** Etichetta che mostra lo stato del server RMI. */
    @FXML
    private Label serverStatusLabel;

    /** Indicatore circolare dello stato del server RMI. */
    @FXML
    private Circle serverStatusDot;

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
        if (hostField.getText().isBlank() || portaField.getText().isBlank() || 
            dbField.getText().isBlank() || usernameField.getText().isBlank()) {
            aggiornaStatoDatabase(false, "Dati mancanti", "Compila tutti i campi obbligatori.");
            return;
        }

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

        Task<Void> taskConnessione = new Task<>() {
            /** Esegue la connessione al database fuori dal thread della GUI. */
            @Override
            protected Void call() throws Exception {
                serverContext.getManagerDB().connetti(host, porta, database, username, password);
                return null;
            }
        };

        taskConnessione.setOnSucceeded(e -> {
            aggiornaStatoDatabase(true, "Database configurato", "Parametri di connessione acquisiti con successo");
        });

        taskConnessione.setOnFailed(e -> {
            Throwable eccezione = taskConnessione.getException();
            aggiornaStatoDatabase(false, "Configurazione non riuscita", eccezione.getMessage());
        });

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

    /**
     * Aggiorna le etichette e gli indicatori grafici dello stato del database.
     *
     * @param successo {@code true} se la configurazione del database è riuscita
     * @param titolo titolo da visualizzare nello stato del database
     * @param dettaglio dettaglio testuale dell'operazione eseguita
     */
    private void aggiornaStatoDatabase(boolean successo, String titolo, String dettaglio) {
        databaseStatusLabel.setText(titolo);
        databaseStatusDetail.setText(dettaglio == null ? "" : dettaglio);
        databaseStatusIcon.setText(successo ? "✓" : "!");
        applicaClasseStato(databaseStatusDot, successo);
        applicaClasseTesto(databaseStatusLabel, successo);
        databaseStatusIcon.getStyleClass().removeAll("status-icon-success", "status-icon-error");
        databaseStatusIcon.getStyleClass().add(successo ? "status-icon-success" : "status-icon-error");
    }

    /**
     * Aggiorna l'etichetta e l'indicatore grafico dello stato del server RMI.
     *
     * @param successo {@code true} se l'avvio del server è riuscito
     * @param messaggio messaggio da visualizzare nello stato del server
     */
    private void aggiornaStatoServer(boolean successo, String messaggio) {
        serverStatusLabel.setText(messaggio);
        applicaClasseStato(serverStatusDot, successo);
        applicaClasseTesto(serverStatusLabel, successo);
    }

    /**
     * Applica all'indicatore circolare la classe CSS corrispondente all'esito
     * dell'operazione.
     *
     * @param indicatore indicatore grafico da aggiornare
     * @param successo {@code true} per lo stato positivo, {@code false} per quello di errore
     */
    private void applicaClasseStato(Circle indicatore, boolean successo) {
        indicatore.getStyleClass().removeAll(
                "status-dot-idle", "status-dot-success", "status-dot-error");
        indicatore.getStyleClass().add(successo ? "status-dot-success" : "status-dot-error");
    }

    /**
     * Applica all'etichetta la classe CSS corrispondente all'esito dell'operazione.
     *
     * @param label etichetta da aggiornare
     * @param successo {@code true} per lo stato positivo, {@code false} per quello di errore
     */
    private void applicaClasseTesto(Label label, boolean successo) {
        label.getStyleClass().removeAll("status-success", "status-error");
        label.getStyleClass().add(successo ? "status-success" : "status-error");
    }

    /**
     * Restituisce un messaggio utile per descrivere un'eccezione.
     *
     * @param errore eccezione da cui ricavare il messaggio
     * @return messaggio dell'eccezione oppure il nome della sua classe se il messaggio è nullo
     */
    private String messaggioErrore(Exception errore) {
        return errore.getMessage() == null ? errore.getClass().getSimpleName() : errore.getMessage();
    }
}
