/*
 * Francesco Girlanda 760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.GestioneEccezioni;
import com.gruppo10.classi.Indirizzi;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteCSV;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller per la schermata di login. Gestisce l'interazione con i campi
 * di username, password e indirizzo, la validazione dei dati di login
 * e la navigazione verso la pagina principale o di registrazione.
 * Si occupa anche di gestire l'accesso per gli utenti non registrati.
 */
public class LoginController extends BasicController{

    public static Utente utenteLoggato = null;

    @FXML
    private TextField indirizzoField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnContinua;

    @FXML
    private Label loginStatus;

    /**
     * Metodo di inizializzazione chiamato automaticamente dal framework JavaFX
     * dopo che tutti gli elementi FXML sono stati iniettati.
     * Aggiunge listener ai campi di testo per abilitare/disabilitare i pulsanti
     * e configura l'autocompletamento per il campo dell'indirizzo.
     */
    public void initialize() {
        usernameField.textProperty().addListener((_, _, _) -> controllaCampi());
        passwordField.textProperty().addListener((_, _, _) -> controllaCampi());
        indirizzoField.textProperty().addListener((_, _, _) -> controllaCampi());

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(indirizzoField, request -> {
            return Indirizzi.getRisultati(request.getUserText());
        });

        loginStatus.setVisible(false);
    }

    /**
     * Controlla lo stato dei campi di testo per abilitare o disabilitare
     * i pulsanti di login e "Continua senza registrarti".
     */
    private void controllaCampi() {
        boolean userPassword = usernameField.getText().isBlank() || passwordField.getText().isBlank();
        boolean indirizzo = indirizzoField.getText().isBlank();

        btnLogin.setDisable(userPassword);
        btnContinua.setDisable(indirizzo);
    }

    /**
     * Gestisce l'evento di clic sul pulsante di login.
     * Cripta la password, cerca l'utente nel file CSV e verifica le credenziali.
     * In caso di successo, imposta l'utente come utente loggato e apre la pagina principale.
     * In caso di errore, mostra un messaggio di stato appropriato.
     */
    @FXML
    public void provaLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashedPassword = Criptatore.cripta(password);

        if (hashedPassword == null)
            return;

        // Verifica se l'utente esiste nel file CSV
        UtenteCSV utenteCSV = new UtenteCSV();
        Utente utente = utenteCSV.cercaUtente(username);
        if (utente == null) {
            loginStatus.setVisible(true);
            loginStatus.setText("UTENTE NON REGISTRATO");
            return;
        }

        if (hashedPassword.equals(utente.getPassword())) {
            utenteLoggato = utente;
            apriPaginaPrincipale();

        } else {
            loginStatus.setVisible(true);
            loginStatus.setText("PASSWORD ERRATA");
        }
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Registrati".
     * Apre la schermata di registrazione.
     */
    @FXML
    private void apriRegistrazione() {
        SceneManager.apriRegistrati(stage, false);
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Continua senza registrarti".
     * Crea un utente temporaneo con ruolo "NON_REGISTRATO" e le coordinate
     * dell'indirizzo inserito, quindi apre la pagina principale.
     */
    @FXML
    public void continuaSenzaRegistrarti() {
        String indirizzo = indirizzoField.getText();
        Coordinate coordinate = new Coordinate(indirizzo);
        if (coordinate.getLat() == null)
            return;
        utenteLoggato = new Utente();
        utenteLoggato.setRuolo("NON_REGISTRATO");
        utenteLoggato.setIndirizzo(indirizzo);
        utenteLoggato.setCords(coordinate);

        apriPaginaPrincipale();
    }

    /**
     * Apre la pagina principale dell'applicazione, mostrando un popup di caricamento
     * per un breve periodo di tempo prima della transizione.
     */
    private void apriPaginaPrincipale() {
        Stage popup = creaPopupLoading();

        // 2. Attendi mezzo secondo prima di aprire la pagina principale
        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
        pause.setOnFinished(_ -> {
            Platform.runLater(() -> {
                try {
                    SceneManager.apriPaginaPrincipale(stage);
                } catch (Exception e) {
                    GestioneEccezioni.errore("Errore caricamento pagina principale", e, false, null);
                } finally {
                    popup.close();
                }
            });
        });
        pause.play();
    }

    /**
     * Crea e visualizza un popup di caricamento modale.
     *
     * @return lo {@link Stage} del popup di caricamento.
     */
    private Stage creaPopupLoading() {
        Stage loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setTitle("Accesso in corso");

        Label label = new Label("Login in corso...");
        label.setStyle("-fx-font-size: 14px;");

        VBox vbox = new VBox(label);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 200, 100);
        loadingStage.setScene(scene);
        loadingStage.setResizable(false);
        loadingStage.show();

        return loadingStage;
    }
}