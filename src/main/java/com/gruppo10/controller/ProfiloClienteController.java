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
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class ProfiloClienteController {

    private Stage stage;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Recensione> recensioni;



    String labelPasswordText = "********"; 
    
    @FXML private TabPane tabPane;
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
        SceneManager.caricaTessere(
            listaRisFiltrata,
            contenitoreTessereRis,
            stage,
            "/GUI/card_ristorante.fxml",
            (controller, _) -> {
                ((CardRistoranteController) controller).setPrincipale(false);
            }
        );

        // Carica recensioni utente
        Path pathRecensioni= Paths.get(System.getProperty("user.dir"), "fileCSV", "recensioni.csv");
        recensioni = RecensioneReader.caricaCSV(pathRecensioni.toString());
        List<Recensione> listaRecFiltrata = filtraRecensioni(recensioni);
        // RecensioneReader.caricaTessere(listaRecFiltrata, contenitoreTessereRec, stage, utenteLoggato.getId());
        SceneManager.caricaTessere(
            listaRecFiltrata,
            contenitoreTessereRec,
            stage,
            "/GUI/card_recensione.fxml",
            (controller, _) -> {
                ((CardRecensioneController) controller).setIdProprietario(utenteLoggato.getId());
                ((CardRecensioneController) controller).setRistorante(null);
                ((CardRecensioneController) controller).setPrincipale(false);
            }
        );
    }

    public void setTab(int tab){
        tabPane.getSelectionModel().select(tab);
    }


    private void caricaDatiUtente() {
        labelNome.setText(utenteLoggato.getNome());
        labelCognome.setText(utenteLoggato.getCognome());
        labelUsername.setText(utenteLoggato.getUsername());
        labelIndirizzo.setText(utenteLoggato.getIndirizzo());
        labelData.setText(utenteLoggato.getDataDiNascita().toString());
        labelRuolo.setText(utenteLoggato.getRuolo().toString());
        labelPassword.setText(labelPasswordText);
    }


    private List<Ristorante> filtraPreferiti(List<Ristorante> listaRistoranti) {
        List<Ristorante> nuovaLista = new ArrayList<>();
        
        for (Ristorante r : listaRistoranti) {
            if (PreferitiReader.controlloPreferito(utenteLoggato.getId(), r.getId())) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }

    private List<Recensione> filtraRecensioni(List<Recensione> listaRecensioni){
        List<Recensione> nuovaLista = new ArrayList<>();

        for(Recensione r : listaRecensioni){
            if(r.getIdUtente() == utenteLoggato.getId()){
                nuovaLista.add(r);
            }
        }

        return nuovaLista;
    }
    

    @FXML
    private void tornaIndietro(){
        SceneManager.apriPaginaPrincipale(stage);
    }


    @FXML
    private void logOut(){
        LoginController.utenteLoggato = null;
        this.utenteLoggato = null;
        SceneManager.logOut(stage);
    }
}
