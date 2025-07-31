package com.gruppo10.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.PreferitiReader;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneReader;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteReader;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProfiloClienteController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Recensione> recensioni;



    String labelPasswordText = "********"; 
    
    @FXML private VBox contenitoreTessereRis;
    @FXML private VBox contenitoreTessereRec;
    @FXML private Label labelNome;
    @FXML private Label labelCognome;
    @FXML private Label labelUsername;
    @FXML private Label labelIndirizzo;
    @FXML private Label labelData;
    @FXML private Label labelRuolo;
    @FXML private Label labelPassword;
    

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
        caricaDatiUtente();

        // Carica ristoranti preferiti
        Path pathRistoranti = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti.csv");
        ristoranti = RistoranteReader.caricaCSV(pathRistoranti.toString());
        List<Ristorante> listaRisFiltrata = filtraPreferiti(ristoranti);
        RistoranteReader.caricaTessere(listaRisFiltrata, contenitoreTessereRis, stage, false);

        // Carica recensioni utente
        Path pathRecensioni= Paths.get(System.getProperty("user.dir"), "fileCSV", "recensioni.csv");
        recensioni = RecensioneReader.caricaCSV(pathRecensioni.toString());
        List<Recensione> listaRecFiltrata = filtraRecensioni(recensioni);
        RecensioneReader.caricaTessere(listaRecFiltrata, contenitoreTessereRec, stage, utenteloggato.getId());
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


    private List<Ristorante> filtraPreferiti(List<Ristorante> listaRistoranti) {
        List<Ristorante> nuovaLista = new ArrayList<>();
        
        for (Ristorante r : listaRistoranti) {
            if (PreferitiReader.controlloPreferito(utenteloggato.getId(), r.getId())) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }

    private List<Recensione> filtraRecensioni(List<Recensione> listaRecensioni){
        List<Recensione> nuovaLista = new ArrayList<>();

        for(Recensione r : listaRecensioni){
            if(r.getIdUtente() == utenteloggato.getId()){
                nuovaLista.add(r);
            }
        }

        return nuovaLista;
    }
    

    @FXML
    private void tornaIndietro(){
        SceneManager.tornaPaginaPrincipale(stage);
    }


    @FXML
    private void logOut(){
        LoginController.utenteLoggato = null;
        this.utenteloggato = null;
        SceneManager.logOut(stage);
    }
}
