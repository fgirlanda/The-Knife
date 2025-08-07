package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RispostaRecensioneController {

    private Recensione recensione;

    private Runnable onCloseCallBack;

    @FXML
    private TextArea txtRisposta;

    @FXML
    private Button btnAnnulla;

    @FXML
    private Button btnInvia;


    @FXML
    private void initialize() {
        txtRisposta.textProperty().addListener((_, _, _) -> controllaCampi());
    }

    public void setRecensione(Recensione recensione) {
        this.recensione = recensione;
    }

    private void controllaCampi() {
        boolean allFieldsFilled = !txtRisposta.getText().isEmpty();
        btnInvia.setDisable(!allFieldsFilled);
    }

    @FXML
    private void aggiungiRisposta() throws Exception {

        String risposta = txtRisposta.getText();

        try {
            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioneCSV.aggiungiRisposta(recensione, risposta);
        } catch (Exception e) {
            return;
        }

        if(onCloseCallBack != null){
            onCloseCallBack.run();
        }
        
        chiudi();
    }

    @FXML
    private void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }

    public String getRisposta(){
        return this.txtRisposta.getText();
    }

    public void setOnCloseCallBack(Runnable callback){
        this.onCloseCallBack = callback;
    }

}