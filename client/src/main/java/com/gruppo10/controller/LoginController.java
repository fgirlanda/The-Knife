/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.gui_elements.GestioneEccezioni;
import com.gruppo10.gui_elements.SceneManager;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.Sessione;
import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.GeocodingException;
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
 * tramite {@code AuthService} e la navigazione verso la pagina principale o
 * di registrazione. Si occupa anche di gestire l'accesso per gli utenti non
 * registrati.
 */
public class LoginController extends BasicController {

    /** Oggetto {@link Sessione}, inizializzato a null, a cui assegnare la sessione ottenuta dopo il login. */
    public static Sessione sessioneCorrente = null;

    /** Campo di testo per l'inserimento dell'indirizzo, in caso di accesso senza registrazione. */
    @FXML
    private TextField indirizzoField;

    /** Campo di testo per l'inserimento dello username. */
    @FXML
    private TextField usernameField;

    /** Campo di testo per l'inserimento della password. */
    @FXML
    private TextField passwordField;

    /** Pulsante per effettuare il login. */
    @FXML
    private Button btnLogin;

    /** Pulsante per continuare senza registrarsi. */
    @FXML
    private Button btnContinua;

    /** Etichetta per mostrare lo stato del login (errore o successo). */
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

        TextFields.<String>bindAutoCompletion(indirizzoField, request -> {
            try {
                return clientContext.getGeoService().suggerimenti(request.getUserText());
            } catch (RemoteException e) {
                return List.of();
            }
        });

        btnLogin.setDefaultButton(true);
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
     * Cripta la password e chiede ad {@code AuthService} di verificare le
     * credenziali sul server. In caso di successo, imposta l'utente come
     * utente loggato e apre la pagina principale.
     * @throws NoSuchAlgorithmException 
     */
    @FXML
    public void provaLogin() throws NoSuchAlgorithmException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashedPassword = "";

        hashedPassword = Criptatore.cripta(password);


        try {
            Sessione sessione = clientContext.getAuthService().login(username, hashedPassword);
            if (sessione == null) {
                loginStatus.setVisible(true);
                loginStatus.setText("CREDENZIALI NON VALIDE");
                return;
            }

            sessioneCorrente = sessione;
            apriPaginaPrincipale();
        } catch (RemoteException e) {
            GestioneEccezioni.errore("Errore durante il login", e, false, null);
        }
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Registrati".
     * Apre la schermata di registrazione.
     */
    @FXML
    private void apriRegistrazione() {
        SceneManager.apriRegistrati(stage, false, clientContext);
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Continua senza registrarti".
     * Geocodifica l'indirizzo tramite {@code GeoService}, crea un utente
     * temporaneo con ruolo "NON_REGISTRATO", quindi apre la pagina
     * principale.
     */
    @FXML
    public void continuaSenzaRegistrarti() {
        String indirizzo = indirizzoField.getText();

        Coordinate coordinate;
        try {
            coordinate = clientContext.getGeoService().geocodifica(indirizzo);
        } catch (GeocodingException e) {
            loginStatus.setVisible(true);
            loginStatus.setText("INDIRIZZO NON TROVATO");
            return;
        } catch (RemoteException e) {
            GestioneEccezioni.errore("Server non raggiungibile", e, false, null);
            return;
        }

        Utente ospite = new Utente();
        ospite.setRuolo("NON_REGISTRATO");
        ospite.setIndirizzo(indirizzo);
        ospite.setCords(coordinate);
        sessioneCorrente = new Sessione(ospite, null);

        apriPaginaPrincipale();
    }

    /**
     * Apre la pagina principale dell'applicazione, mostrando un popup di caricamento
     * per un breve periodo di tempo prima della transizione.
     */
    private void apriPaginaPrincipale() {
        Stage popup = creaPopupLoading();

        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
        pause.setOnFinished(_ -> {
            Platform.runLater(() -> {
                try {
                    SceneManager.apriPaginaPrincipale(stage, clientContext);
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