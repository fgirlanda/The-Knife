package com.gruppo10.controller;

import java.io.IOException;

import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfiloRistoratoreController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
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
            dialogStage.initModality(Modality.APPLICATION_MODAL); // blocca l'interazione con altre finestre
            dialogStage.initOwner(stage); // stage principale, da passare se disponibile
            dialogStage.setScene(new Scene(root));

            // Se il controller ha bisogno dello stage, puoi passarlo:
            AggiungiRistoranteController controller = loader.getController();
            controller.setStage(dialogStage); // opzionale, se hai un metodo per settarlo

            dialogStage.showAndWait(); // mostra e attende la chiusura

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}