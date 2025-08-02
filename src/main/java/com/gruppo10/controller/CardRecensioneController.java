package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardRecensioneController {

    private Stage stage;
    
    private Recensione recensione;

    private Utente utenteLoggato = LoginController.utenteLoggato; 

    private int idProprietario;

    
    @FXML private HBox card;

    @FXML private Text txtCliente;

    @FXML private Text txtTesto;

    @FXML private Text txtStelle;

    @FXML private Text txtRisposta;

    @FXML private Button btnRispondi;

    @FXML private Button btnRimuovi;

    @FXML private Button btnModifica;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setIdProprietario(int idProprietario){
        this.idProprietario = idProprietario;
    }

    public void setRecensione(Recensione recensione){
        this.recensione = recensione;
        if(utenteLoggato.getRuolo() == Ruolo.CLIENTE || utenteLoggato.getId() != this.idProprietario){
            btnRispondi.setVisible(false);
        }
        if(utenteLoggato.getId() != this.recensione.getIdUtente())
        {
            btnRimuovi.setVisible(false);
            btnModifica.setVisible(false);
        }
    }
    

    public void setDati(){
        txtCliente.setText(this.recensione.getNomeUtente());
        txtTesto.setText(this.recensione.getTesto());
        txtStelle.setText(String.format("%d",this.recensione.getStelle()) + " ★");
        txtRisposta.setText(this.recensione.getRisposta());
    }


    @FXML
    private void rispondi(){
        SceneManager.finestraDialogo("/GUI/rispondi_recensione.fxml", "Rispondi", stage, (RispostaRecensioneController controller) -> {
            controller.setStage(stage);
            controller.setRecensione(this.recensione);
        });
    }

    
    @FXML
    private void apriModifica(){
        SceneManager.finestraDialogo("/GUI/modifica_recensione.fxml", "Modifica", stage, (ModificaRecensioneController controller) -> controller.setRecensione(this.recensione));
    }

    
    @FXML
    private void rimuovi(){
        RecensioneWriter.rimuoviRecensione(this.recensione);
        
    };
    
}