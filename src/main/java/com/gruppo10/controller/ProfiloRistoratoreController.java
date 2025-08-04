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
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfiloRistoratoreController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Ristorante> listaFiltrata;

    @FXML private TabPane tabPane;
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
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti.csv");
        ristoranti = RistoranteReader.caricaCSV(path.toString());
        listaFiltrata = filtraProprietario(ristoranti);
        aggiornaContenitore(listaFiltrata);
    }

    public void setTab(int tab){
        tabPane.getSelectionModel().select(tab);
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


    private List<Ristorante> filtraProprietario(List<Ristorante> listaRistoranti){
        List<Ristorante> nuovaLista = new ArrayList<>();

        for (Ristorante r : listaRistoranti) {
            if (r.getIdproprietario() == utenteloggato.getId()) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }


    @FXML
    private void apriAggiungiRistorante() {
        SceneManager.finestraDialogo("/GUI/aggiungi_ristorante.fxml", "Aggiungi Ristorante", stage,
            (AggiungiRistoranteController controller) -> {
                controller.setOnCloseCallback(() -> {
                    // Qui aggiungi il nuovo ristorante nella lista e aggiorni il contenitore
                    Ristorante r = controller.getNuovoRistorante();
                    if (r != null) {
                        listaFiltrata.add(r);
                        aggiornaContenitore(listaFiltrata);
                    }
                });
            });
    }

    
    @FXML
    public void tornaIndietro(){
        SceneManager.apriPaginaPrincipale(stage);
    }


    @FXML
    private void logOut(){
        LoginController.utenteLoggato = null;
        this.utenteloggato = null;
        SceneManager.logOut(stage);
    }

    private void aggiornaContenitore(List<Ristorante> lista){
        SceneManager.caricaTessere(
            lista,
            contenitoreTessere,
            stage,
            "/GUI/card_ristorante.fxml",
            (controller, _) -> {
                ((CardRistoranteController) controller).setPrincipale(false);
            }
        );
    }
}