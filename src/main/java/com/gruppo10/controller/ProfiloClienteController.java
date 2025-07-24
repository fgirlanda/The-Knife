package com.gruppo10.controller;

import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfiloClienteController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

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
    String labelPasswordText = "********"; 

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
        caricaDatiUtente();
    }

    private void caricaDatiUtente() {
        labelNome.setText(utenteloggato.getNome());
        labelCognome.setText(utenteloggato.getCognome());
        labelUsername.setText(utenteloggato.getUsername());
        labelIndirizzo.setText(utenteloggato.getIndirizzo());
        labelData.setText(utenteloggato.getDataDiNascita().toString());
        labelRuolo.setText(utenteloggato.getRuolo().toString());
        labelPassword.setText(labelPasswordText);
    }

    public void modificaDati(){
    }

    public void tornaIndietro(){
        SceneManager.cambioScena(stage, "/GUI/pagina_principale.fxml", "The Knife", 
            (PaginaPrincipaleController controller) -> controller.setStage(stage));
    }
}
