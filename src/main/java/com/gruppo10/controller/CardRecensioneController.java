package com.gruppo10.controller;

import java.util.List;

import com.gruppo10.classi.CardController;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneReader;
import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardRecensioneController implements CardController<Recensione>{

    private Stage stage;
    
    private Recensione recensione;

    private Ristorante ristorante;

    private List<Recensione> listaRecensioni;

    private boolean paginaPrincipale;

    private Utente utenteLoggato = LoginController.utenteLoggato; 

    private int idProprietario;

    private VBox contenitore;
    
    @FXML private HBox card;

    @FXML private Text txtCliente;

    @FXML private Text txtTesto;

    @FXML private Text txtStelle;

    @FXML private Text txtRisposta;

    @FXML private Button btnRispondi;

    @FXML private Button btnRimuovi;

    @FXML private Button btnModifica;


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRistorante(Ristorante ristorante){
        this.ristorante = ristorante;
    }

    public void setIdProprietario(int idProprietario){
        this.idProprietario = idProprietario;
    }

    public void setPrincipale(boolean paginaPrincipale){
        this.paginaPrincipale = paginaPrincipale;
    }


    public void setListaRecensioni(List<Recensione> listaRecensioni){
        this.listaRecensioni = listaRecensioni;
    }


    @Override
    public void setItem(Recensione recensione, VBox contenitore){
        this.recensione = recensione;
        String titolo = stage.getTitle().toLowerCase();
        if(utenteLoggato.getRuolo() == Ruolo.CLIENTE || utenteLoggato.getId() != this.idProprietario){
            btnRispondi.setVisible(false);
        }
        if(utenteLoggato.getId() != this.recensione.getIdUtente() || titolo.equals("the knife - profilo"))
        {
            btnRimuovi.setVisible(false);
            btnModifica.setVisible(false);
        }

        this.contenitore = contenitore;
    }
    

    @Override
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
        SceneManager.finestraDialogo("/GUI/modifica_recensione.fxml", "Modifica", stage, (ModificaRecensioneController controller) -> {
            controller.setStage(stage);
            controller.setPrincipale(paginaPrincipale);
            controller.setRecensione(this.recensione, this.contenitore);
            controller.setRistorante(ristorante);
        });
    }


    @FXML
    private void rimuovi(){
        this.ristorante.rimuoviRecensione(this.recensione);
        RecensioneWriter.rimuoviRecensione(this.recensione);
        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale);
    }
}