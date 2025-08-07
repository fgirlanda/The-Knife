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
import com.gruppo10.classi.UtenteReader;

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

public class LoginController {

    public static Utente utenteLoggato = null;

    private Stage stage;

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

    // Aggiungere controllaCampi
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

    private void controllaCampi() {
        boolean userPassword = usernameField.getText().isBlank() || passwordField.getText().isBlank();
        boolean indirizzo = indirizzoField.getText().isBlank();

        btnLogin.setDisable(userPassword);
        btnContinua.setDisable(indirizzo);
    }

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void provaLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashedPassword = Criptatore.cripta(password);

        if (hashedPassword == null)
            return;

        // Verifica se l'utente esiste nel file CSV
        Utente utente = UtenteReader.cercaUtente(username);
        if (utente == null) {
            loginStatus.setVisible(true);
            loginStatus.setText("Login status: UTENTE NON REGISTRATO");
            return;
        }

        if (hashedPassword.equals(utente.getPassword())) {
            utenteLoggato = utente;
            apriPaginaPrincipale();

        } else {
            loginStatus.setVisible(true);
            loginStatus.setText("Login status: PASSWORD ERRATA");
        }
    }

    @FXML
    private void apriRegistrazione() {
        SceneManager.apriRegistrati(stage, false);
    }

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

    private void apriPaginaPrincipale() {
        Stage popup = creaPopupLoading();

        // 2. Attendi mezzo secondo prima di aprire la pagina principale
        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
        pause.setOnFinished(_ -> {
            Platform.runLater(() -> {
                try {
                    SceneManager.apriPaginaPrincipale(stage);
                } catch (Exception e) {
                    GestioneEccezioni.errore("Errore caricamento pagina", e.getMessage(), false, null);
                } finally {
                    popup.close();
                }
            });
        });
        pause.play();
    }

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
