package com.gruppo10.controller;

import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ProfiloRistoratoreController extends Controller{

    private Utente utenteloggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Ristorante> listaFiltrata;

    @FXML
    private VBox contenitoreTessere;

    public void caricaDati(){
        caricaDatiUtente(utenteloggato);
        RistoranteCSV ristoranteCSV = new RistoranteCSV();
        ristoranti = ristoranteCSV.caricaCSV();
        if (ristoranti != null) {
            listaFiltrata = filtraProprietario(ristoranti);
            aggiornaContenitore(listaFiltrata);
        }
    }

    private List<Ristorante> filtraProprietario(List<Ristorante> listaRistoranti) {
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
                        Ristorante r = controller.getNuovoRistorante();
                        if (r != null) {
                            listaFiltrata.add(r);
                            aggiornaContenitore(listaFiltrata);
                        }
                    });
                });
    }

    @FXML
    public void tornaIndietro() {
        SceneManager.apriPaginaPrincipale(stage);
    }

    @FXML
    private void logOut() {
        LoginController.utenteLoggato = null;
        this.utenteloggato = null;
        SceneManager.logOut(stage);
    }

    private void aggiornaContenitore(List<Ristorante> lista) {
        SceneManager.caricaTessere(
                lista,
                contenitoreTessere,
                stage,
                "/GUI/card_ristorante.fxml",
                (controller, _) -> {
                    ((CardRistoranteController) controller).setPrincipale(false);
                    ((CardRistoranteController) controller).setStage(stage);
                    ((CardRistoranteController) controller).setOnClick();
                });
    }
}