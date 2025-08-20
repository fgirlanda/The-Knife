/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class BasicController {

    private Utente utenteLoggato = LoginController.utenteLoggato;
    
    protected Stage stage;

    protected boolean paginaPrincipale;

    @FXML
    protected Button btnAnnulla;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelCognome;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelIndirizzo;
    @FXML
    private Label labelData;
    @FXML
    private Label labelRuolo;
    @FXML
    private Label labelPassword;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    @FXML
    public void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }

    public void disabilitaBottone(Button bottone, boolean abilita){
        bottone.setDisable(abilita);
    }
    
    // Profilo cliente/ristoratore
    public void caricaDatiUtente() {
        String labelPasswordText = "********";
        labelNome.setText(utenteLoggato.getNome());
        labelCognome.setText(utenteLoggato.getCognome());
        labelUsername.setText(utenteLoggato.getUsername());
        labelIndirizzo.setText(utenteLoggato.getIndirizzo());
        labelData.setText(utenteLoggato.getDataDiNascita().toString());
        labelRuolo.setText(utenteLoggato.getRuolo().toString());
        labelPassword.setText(labelPasswordText);
    }

    public void setTab(int tab) {
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    public void logOut() {
        LoginController.utenteLoggato = null;
        utenteLoggato = null;
        SceneManager.apriLogin(stage);
    }
}
