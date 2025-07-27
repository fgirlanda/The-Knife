package com.gruppo10.controller;

import com.gruppo10.classi.Ristorante;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PaginaRistoranteController {

    private Stage stage;

    private Ristorante ristorante;

    @FXML private Label txtIndirizzo;

    @FXML private Label txtMediaRec;

    @FXML private Label txtPrezzo;
    
    @FXML private Label txtNomeRistorante;
    
    @FXML private Label txtDescrizione;

    @FXML private ImageView btnPreferiti;

    @FXML private Button btnIndietro;

    @FXML private Button btnAggiungiRecensione;


    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRistorante(Ristorante ristorante){
        this.ristorante = ristorante;
    }

    public void setDati(){
        txtIndirizzo.setText(this.ristorante.getIndirizzo());
        // txtMediaRec.setText(this.ristorante.getMediaRec().toString());
        txtPrezzo.setText(this.ristorante.getPrezzo());
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtDescrizione.setText(this.ristorante.getDescrizione());
    }
}
