package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardRecensioneController {

    private Stage stage;
    
    public Recensione recensione;
    
    @FXML private HBox card;
    @FXML private Text txtTesto;
    @FXML private Text txtStelle;
    @FXML private Text txtRisposta;


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setRecensione(Recensione recensione){
      this.recensione = recensione;
    }
    

    public void setDati(){
        txtTesto.setText(this.recensione.getTesto());
        txtStelle.setText(String.format("%d",this.recensione.getStelle()) + " ★");
        txtRisposta.setText(this.recensione.getRisposta());
    }


    public void rispondi(){
        SceneManager.finestraDialogo("/GUI/rispondi_recensione.fxml", "Rispondi", stage, (RispostaRecensioneController controller) -> {
            controller.setStage(stage);
            controller.setRecensione(this.recensione);
        });
    }
}