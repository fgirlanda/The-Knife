package com.gruppo10.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteReader;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfiloRistoratoreController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

    static List<Ristorante> ristoranti;

    @FXML private VBox contenitoreTessere;
    @FXML private Label labelNome;
    @FXML private Label labelCognome;
    @FXML private Label labelUsername;
    @FXML private Label labelIndirizzo;
    @FXML private Label labelData;
    @FXML private Label labelRuolo;
    @FXML private Label labelPassword;
    String labelPasswordText = "********"; 


    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
        caricaDatiUtente();
    }

    @FXML
    private void initialize() {
        // Caricamento schede ristorante
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti.csv");
        ristoranti = RistoranteReader.caricaCSV(path.toString());
        List<Ristorante> listaFiltrata = filtraProprietario(ristoranti);
        RistoranteReader.caricaTessere(listaFiltrata, contenitoreTessere);
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


    @FXML
    private void apriAggiungiRistorante() {
        SceneManager.finestraDialogo("/GUI/aggiungi_ristorante.fxml", "Aggiungi Ristorante", stage,
            (AggiungiRistoranteController controller) -> controller.setStage(stage));
    }

    
    @FXML
    public void tornaIndietro(){
        SceneManager.cambioScena(stage, "/GUI/pagina_principale.fxml", "The Knife", 
        (PaginaPrincipaleController controller) -> controller.setStage(stage));
    }

    public List<Ristorante> filtraProprietario(List<Ristorante> listaRistoranti){
        List<Ristorante> nuovaLista = new ArrayList<>();

        for (Ristorante r : listaRistoranti) {
            if (r.getIdproprietario() == utenteloggato.getId()) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }
}