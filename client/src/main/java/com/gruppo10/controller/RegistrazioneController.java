/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import javafx.scene.control.Label;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.gui_elements.GestioneEccezioni;
import com.gruppo10.gui_elements.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.UsernameGiaEsistenteException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Controller per la schermata di registrazione. Gestisce l'interazione
 * con i campi di registrazione, la validazione dei dati inseriti e la
 * creazione di un nuovo utente nel sistema.
 */
public class RegistrazioneController extends BasicController {

    /** RadioButton per la selezione del ruolo Cliente */
    @FXML
    private RadioButton radioCliente;

    /** RadioButton per la selezione del ruolo Ristoratore */
    @FXML
    private RadioButton radioRistoratore;

    /** Campo di input per il nome */
    @FXML
    private TextField nomeField;

    /** Campo di input per il cognome */
    @FXML
    private TextField cognomeField;

    /** Campo di input per lo username */
    @FXML
    private TextField usernameField;

    /** Campo di input per l'indirizzo */
    @FXML
    private TextField indirizzoField;

    /** Campo di input per la password */
    @FXML
    private PasswordField passwordField;

    /** Selettore per la data di nascita */
    @FXML
    private DatePicker dataNascitaPicker;

    /** Pulsante per confermare la registrazione */
    @FXML
    private Button btnRegistrati;

    /** Label per mostrare lo stato della registrazione o messaggi di errore */
    @FXML
    private Label statusRegistrazione;

    /** Gruppo di Toggle per selezionare il ruolo */
    @FXML
    private ToggleGroup ruoloGroup;

    /**
     * Metodo di inizializzazione chiamato automaticamente dal framework JavaFX
     * dopo che tutti gli elementi FXML sono stati iniettati.
     * Aggiunge listener a tutti i campi di input per controllare lo stato
     * di validità e abilita l'autocompletamento per il campo indirizzo.
     */
    public void initialize() {

        nomeField.textProperty().addListener((_, _, _) -> controllaCampi());
        cognomeField.textProperty().addListener((_, _, _) -> controllaCampi());
        usernameField.textProperty().addListener((_, _, _) -> controllaCampi());
        passwordField.textProperty().addListener((_, _, _) -> controllaCampi());
        indirizzoField.textProperty().addListener((_, _, _) -> controllaCampi());
        dataNascitaPicker.valueProperty().addListener((_, _, _) -> controllaCampi());
        ruoloGroup.selectedToggleProperty().addListener((_, _, _) -> controllaCampi());
        btnRegistrati.setDefaultButton(true);
        TextFields.<String>bindAutoCompletion(indirizzoField, request -> {
            try {
                return clientContext.getGeoService().suggerimenti(request.getUserText());
            } catch (RemoteException e) {
                return List.of();
            }
        });
    }

    /**
     * Controlla se tutti i campi di input sono stati compilati.
     * Abilita o disabilita il pulsante di registrazione in base allo stato dei
     * campi.
     */
    private void controllaCampi() {
        boolean campiVuoti = nomeField.getText().isBlank() ||
                cognomeField.getText().isBlank() ||
                usernameField.getText().isBlank() ||
                passwordField.getText().isBlank() ||
                indirizzoField.getText().isBlank() ||
                dataNascitaPicker.getValue() == null ||
                ruoloGroup.getSelectedToggle() == null;

        disabilitaBottone(btnRegistrati, campiVuoti);
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Registrati".
     * Acquisisce i dati dai campi, esegue le validazioni necessarie (indirizzo,
     * username esistente), cripta la password e crea un nuovo oggetto Utente.
     * Salva quindi il nuovo utente nel database e naviga alla pagina di login.
     * <p>
     * Gestione errori:
     * </p>
     * <ul>
     * <li>Coordinate: a {@code lat} viene assegnato null, il nuovo {@link Utente}
     * non viene creato e la pagina di registrazione rimane aperta per permettere
     * eventualmente il reinserimento dei dati.
     * <li>Criptatore: a {@code password} viene assegnato null, poi la gestione è
     * analoga a Coordinate.
     * <li>Scrittura sul database: se viene sollevata un'eccezione viene gestita
     * tramite {@link com.gruppo10.classi.GestioneEccezioni}, l'utente viene notificato e
     * la pagina di registrazione rimane aperta per permettere eventualmente il
     * reinserimento dei dati
     * </ul>
     */
    @FXML
    public void registrati() {
        RadioButton selectedRadioButton = (RadioButton) ruoloGroup.getSelectedToggle();
        String ruolo = selectedRadioButton.getText();
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String indirizzo = indirizzoField.getText();

        if (indirizzo.length() < 20) {
            statusRegistrazione.setText("Seleziona un indirizzo valido");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataNascita = dataNascitaPicker.getValue().format(formatter);

        try {
            password = Criptatore.cripta(password);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (password == null)
            return;

        // Coordinate coordinate = new Coordinate(indirizzo);
        // if (coordinate.getLat() == null)
        //     return;

        Coordinate coordinate;

        try{
            coordinate = clientContext.getGeoService().geocodifica(indirizzo);
        } catch (Exception e) {
            GestioneEccezioni.errore("Errore durante la geocodifica dell'indirizzo", e, false, null);
            return;
        }

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setUsername(username);
        utente.setPassword(password);
        utente.setDataDiNascita(dataNascita);
        utente.setIndirizzo(indirizzo);
        utente.setRuolo(ruolo);
        utente.setCords(coordinate);

        try {
            clientContext.getAuthService().registrati(utente);
        } catch (RemoteException e) {
            GestioneEccezioni.errore("Errore durante la registrazione", e, false, null);
        } catch (UsernameGiaEsistenteException e) {
            GestioneEccezioni.errore("Errore durante la registrazione", e, false, null);
        }
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Annulla".
     * A seconda della pagina precedente (pagina principale o login),
     * naviga alla schermata appropriata.
     */
    @FXML
    private void annulla() {
        if (paginaPrincipale) {
            SceneManager.apriPaginaPrincipale(stage);
        } else {
            apriLogin();
        }
    }

    /**
     * Apre la schermata di login.
     */
    private void apriLogin() {
        SceneManager.apriLogin(stage);
    }
}
