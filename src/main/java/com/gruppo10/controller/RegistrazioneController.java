package com.gruppo10.controller;

import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.GestioneEccezioni;
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
import javafx.stage.Stage;

import org.controlsfx.control.textfield.TextFields;

public class RegistrazioneController {

    private Stage stage;

    private boolean paginaPrincipale;

    @FXML
    private RadioButton clienteRadioButton;

    @FXML
    private RadioButton ristoratoreRadioButton;

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

    public void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        nomeField.textProperty().addListener((_, _, _) -> controllaCampi()); // (_, _, _) = (observable, oldValue,
                                                                             // newValue)
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

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    private void controllaCampi() {
        boolean allFieldsFilled = nomeField.getText().isBlank() ||
                cognomeField.getText().isBlank() ||
                usernameField.getText().isBlank() ||
                passwordField.getText().isBlank() ||
                indirizzoField.getText().isBlank() ||
                dataNascitaPicker.getValue() == null ||
                ruoloGroup.getSelectedToggle() == null;
        btnRegistrati.setDisable(allFieldsFilled);
    }

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

        // Crea un oggetto Utente e imposta i valori
        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setUsername(username);
        utente.setPassword(password);
        utente.setDataDiNascita(dataNascita);
        utente.setIndirizzo(indirizzo);
        utente.setRuolo(ruolo);
        utente.setCords(coordinate);

        utenteCSV.aggiungiUtente(utente);
        try {
            utenteCSV.scrivi(utente);
        } catch (Exception e) {
            GestioneEccezioni.errore("Errore di registrazione", e.getMessage(), false, null);
            return;
        }
        apriLogin();
    }

    @FXML
    private void chiudi() {
        if (paginaPrincipale) {
            SceneManager.cambioScena(stage, "/GUI/pagina_principale.fxml", "The Knife - Login", (PaginaPrincipaleController controller) -> controller.setStage(stage));
        } else {
            apriLogin();
        }
    }

    private void apriLogin() {
        SceneManager.cambioScena(stage, "/GUI/login.fxml", "The Knife - Login",
                (LoginController controller) -> controller.setStage(stage));
    }
}
