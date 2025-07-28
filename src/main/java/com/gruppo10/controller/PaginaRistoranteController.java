package com.gruppo10.controller;

import java.io.IOException;

import com.gruppo10.classi.PreferitiReader;
import com.gruppo10.classi.PreferitiWriter;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PaginaRistoranteController {

    private Stage stage;

    private Ristorante ristorante;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    @FXML private Label txtIndirizzo;

    @FXML private Label txtMediaRec;

    @FXML private Label txtPrezzo;
    
    @FXML private Label txtNomeRistorante;
    
    @FXML private Label txtDescrizione;

    @FXML private ImageView imagePreferiti;

    @FXML private Button btnIndietro;

    @FXML private Button btnAggiungiRecensione;

    @FXML private Button btnPreferiti;




 
    
    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;

    }

    public void setRistorante(Ristorante ristorante){
        this.ristorante = ristorante;

        if(utenteLoggato.getRuolo().toString().equals("RISTORATORE")) {
            btnAggiungiRecensione.setVisible(false);
            btnPreferiti.setVisible(false);
        }
        if (PreferitiReader.controlloPreferito(utenteLoggato.getId(), ristorante.getId())) {
            imagePreferiti.setImage(new ImageView("/images/cuore_pieno.png").getImage());
        } else {
            imagePreferiti.setImage(new ImageView("/images/cuore_vuoto.png").getImage());
        }
    }

    public void setDati(){
        txtIndirizzo.setText(this.ristorante.getIndirizzo());
        // txtMediaRec.setText(this.ristorante.getMediaRec().toString());
        txtPrezzo.setText(this.ristorante.getPrezzo());
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtDescrizione.setText(this.ristorante.getDescrizione());
    }

    @FXML
    private void aggiungiRecensione(){
        SceneManager.finestraDialogo("/GUI/aggiungi_recensione.fxml", "Aggiungi Recensione", stage,
            (AggiungiRecensioneController controller) -> controller.setStage(stage));
    }

    @FXML
    private void tornaIndietro(){
        SceneManager.tornaPaginaPrincipale(stage);
    }

    @FXML
    private void gestisciPreferiti() {
        try {
            if(imagePreferiti.getImage().getUrl().contains("cuore_vuoto.png")) {
                imagePreferiti.setImage(new ImageView("/images/cuore_pieno.png").getImage());
                aggiungiPreferito();
            } else {
                imagePreferiti.setImage(new ImageView("/images/cuore_vuoto.png").getImage());
                rimuoviPreferito();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aggiungiPreferito() {
        try {
            if(!PreferitiReader.controlloPreferito(utenteLoggato.getId(), ristorante.getId())) {
                PreferitiWriter.aggiungiPreferito(utenteLoggato.getId(), ristorante.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

   }

   private void rimuoviPreferito() {
       try {
           PreferitiWriter.rimuoviPreferito(utenteLoggato.getId(), ristorante.getId());
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}