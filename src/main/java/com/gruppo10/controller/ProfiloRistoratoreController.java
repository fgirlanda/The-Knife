package com.gruppo10.controller;

import java.io.IOException;

import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfiloRistoratoreController {

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

    // Imposta il riferimento alla finestra principale (Stage)
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

    @FXML
    private void apri_aggiungi_ristorante() {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/aggiungi_ristorante.fxml"));
            Parent root = loader.load();

            // Crea un nuovo stage per il dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aggiungi Ristorante");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre
            dialogStage.initOwner(stage);
            dialogStage.setScene(new Scene(root));

            AggiungiRistoranteController controller = loader.getController();
            controller.setStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void modificaDati(){
    }
    public void tornaIndietro(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pagina_principale.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Pagina Principale");
            PaginaPrincipaleController controller = loader.getController();
            controller.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}