package com.gruppo10.controller;

import com.gruppo10.classi.Ristorante;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardRistoranteController {

    private Stage stage;

    @FXML private ImageView imgRistorante;
    @FXML private Button btnPreferito;
    @FXML private Text txtNomeRistorante;
    @FXML private Text txtRecensioni;
    @FXML private Text txtPrezzo;
    @FXML private Text txtTipoCucina;

    public void setDati(Ristorante ristorante){

        txtNomeRistorante.setText(ristorante.getNomeRistorante());
        //txtRecensioni.setText(ristorante.getRecensioni().size() + " Recensioni");
        txtPrezzo.setText(ristorante.getPrezzo());
        txtTipoCucina.setText(ristorante.getTipoCucina().toString());
    }



    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
