package com.gruppo10.controller;

import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.GestionePreferiti;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
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

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox contenitoreTessereRis;
    @FXML
    private VBox contenitoreTessereRec;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelCognome;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelIndirizzo;
    @FXML
    private Label labelData;
    @FXML
    private Label labelRuolo;
    @FXML
    private Label labelPassword;

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
        caricaDatiUtente();

        // Carica ristoranti preferiti
        RistoranteCSV ristoranteCSV = new RistoranteCSV();
        ristoranti = ristoranteCSV.caricaCSV();
        if (ristoranti != null) {
            List<Ristorante> listaRisFiltrata = filtraPreferiti(ristoranti);
            SceneManager.caricaTessere(
                    listaRisFiltrata,
                    contenitoreTessereRis,
                    stage,
                    "/GUI/card_ristorante.fxml",
                    (controller, _) -> {
                        ((CardRistoranteController) controller).setPrincipale(false);
                        ((CardRistoranteController) controller).setStage(stage);
                        ((CardRistoranteController) controller).setOnClick();
                    });

            // Carica recensioni utente
            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioni = recensioneCSV.caricaCSV();
            if (recensioni != null) {
                List<Recensione> listaRecFiltrata = filtraRecensioni(recensioni);
                SceneManager.caricaTessere(
                        listaRecFiltrata,
                        contenitoreTessereRec,
                        stage,
                        "/GUI/card_recensione.fxml",
                        (controller, _) -> {
                            // ((CardRecensioneController) controller).setRistorante(null); // Necessario se
                            // si vuole fare in modo di aprire la pagina ristorante dalla card recensione
                            ((CardRecensioneController) controller).setPrincipale(false);
                        });
            }
        }

    }

    public void setTab(int tab) {
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
            if (GestionePreferiti.controlloPreferito(utenteLoggato.getId(), r.getId())) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }

    private List<Recensione> filtraRecensioni(List<Recensione> listaRecensioni) {
        List<Recensione> nuovaLista = new ArrayList<>();

        for (Recensione r : listaRecensioni) {
            if (r.getIdUtente() == utenteLoggato.getId()) {
                nuovaLista.add(r);
            }
        }

        return nuovaLista;
    }

    @FXML
    private void tornaIndietro() {
        SceneManager.apriPaginaPrincipale(stage);
    }

    @FXML
    private void logOut() {
        LoginController.utenteLoggato = null;
        this.utenteLoggato = null;
        SceneManager.logOut(stage);
    }
}
