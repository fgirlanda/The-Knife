/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.Indirizzi;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteCSV;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.controlsfx.control.textfield.TextFields;

/**
 * Controller per la schermata di registrazione. Gestisce l'interazione
 * con i campi di registrazione, la validazione dei dati inseriti e la
 * creazione di un nuovo utente nel sistema.
 */
public class RegistrazioneController extends BasicController {

    @FXML
    private RadioButton radioCliente;

    @FXML
    private RadioButton radioRistoratore;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField cognomeField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField indirizzoField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker dataNascitaPicker;

    @FXML
    private Button btnRegistrati;

    @FXML
    private Label statusRegistrazione;

    @FXML
    private ToggleGroup ruoloGroup;

    /**
     * Metodo di inizializzazione chiamato automaticamente dal framework JavaFX
     * dopo che tutti gli elementi FXML sono stati iniettati.
     * Aggiunge listener a tutti i campi di input per controllare lo stato
     * di validità e abilita l'autocompletamento per il campo indirizzo.
     */
    public void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        nomeField.textProperty().addListener((_, _, _) -> controllaCampi()); 
        cognomeField.textProperty().addListener((_, _, _) -> controllaCampi());
        usernameField.textProperty().addListener((_, _, _) -> controllaCampi());
        passwordField.textProperty().addListener((_, _, _) -> controllaCampi());
        indirizzoField.textProperty().addListener((_, _, _) -> controllaCampi());
        dataNascitaPicker.valueProperty().addListener((_, _, _) -> controllaCampi());
        ruoloGroup.selectedToggleProperty().addListener((_, _, _) -> controllaCampi());

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(indirizzoField, request -> {
            return Indirizzi.getRisultati(request.getUserText());
        });
    }

    /**
     * Controlla se tutti i campi di input sono stati compilati.
     * Abilita o disabilita il pulsante di registrazione in base allo stato dei campi.
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
     * Salva quindi il nuovo utente nel file CSV e naviga alla pagina di login.
     */
    @FXML
    public void registrati() {
        UtenteCSV utenteCSV = new UtenteCSV();
        RadioButton selectedRadioButton = (RadioButton) ruoloGroup.getSelectedToggle();
        String ruolo = selectedRadioButton.getText();
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String indirizzo = indirizzoField.getText();

        if(indirizzo.length() < 20){
            statusRegistrazione.setText("Seleziona un indirizzo valido");
            return;
        }

        if (utenteCSV.cercaUtente(username) != null) {
            statusRegistrazione.setText("Username già in uso. Scegli un altro username.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataNascita = dataNascitaPicker.getValue().format(formatter);

        password = Criptatore.cripta(password);

        if (password == null)
            return;

        Coordinate coordinate = new Coordinate(indirizzo);
        if (coordinate.getLat() == null)
            return;

        Utente utente = new Utente();
        utente.setId(utenteCSV.ultimoID());
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setUsername(username);
        utente.setPassword(password);
        utente.setDataDiNascita(dataNascita);
        utente.setIndirizzo(indirizzo);
        utente.setRuolo(ruolo);
        utente.setCords(coordinate);

        try {
            utenteCSV.aggiungiUtente(utente);
            utenteCSV.scrivi(utente);
        } catch (Exception e) {
            return;
        }
        apriLogin();
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