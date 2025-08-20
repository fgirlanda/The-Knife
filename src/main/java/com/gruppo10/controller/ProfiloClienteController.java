/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.PreferitiCSV;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Controller per la vista del profilo cliente. Estende {@link BasicController}
 * e gestisce il caricamento e la visualizzazione dei dati utente, dei ristoranti
 * preferiti e delle recensioni inserite dal cliente.
 */
public class ProfiloClienteController extends BasicController{

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Recensione> recensioni;

    private PreferitiCSV preferitiCSV = new PreferitiCSV();
    
    private RistoranteCSV ristoranteCSV = new RistoranteCSV();
    @FXML
    private VBox contenitoreTessereRis;
    @FXML
    private VBox contenitoreTessereRec;


    /**
     * Carica i dati del profilo dell'utente loggato.
     * Recupera e visualizza la lista dei ristoranti preferiti e le recensioni
     * scritte dall'utente, popolando i rispettivi contenitori.
     */
    public void caricaDati(){
        caricaDatiUtente();

        // Carica ristoranti preferiti
        ristoranti = ristoranteCSV.caricaCSV();
        ristoranteCSV.creaMappa(ristoranti);
        if (ristoranti != null) {
            List<Ristorante> listaRisFiltrata = filtraPreferiti(ristoranti);
            SceneManager.caricaTessere(
                    listaRisFiltrata,
                    contenitoreTessereRis,
                    stage,
                    "card_ristorante.fxml",
                    (controller, _) -> {
                        ((CardRistoranteController) controller).setPrincipale(false);
                        ((CardRistoranteController) controller).setStage(stage);
                        ((CardRistoranteController) controller).setOnClick();
                        ((CardRistoranteController) controller).setIndiceTab(1);
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
                        "card_recensione.fxml",
                        (controller, _) -> {
                            ((CardRecensioneController) controller).setPrincipale(false);
                            ((CardRecensioneController) controller).setIndiceTab(2);
                        });
            }
        }
    }


    /**
     * Filtra la lista di tutti i ristoranti per trovare solo quelli
     * che sono stati aggiunti ai preferiti dall'utente loggato.
     *
     * @param listaRistoranti la lista completa dei ristoranti.
     * @return una nuova lista contenente solo i ristoranti preferiti.
     */
    private List<Ristorante> filtraPreferiti(List<Ristorante> listaRistoranti) {
        List<Ristorante> nuovaLista = new ArrayList<>();

        for (Ristorante r : listaRistoranti) {
            if (preferitiCSV.controlloPreferito(utenteLoggato.getIdUtente(), r.getIdRistorante())) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }

    /**
     * Filtra la lista di tutte le recensioni per trovare solo quelle
     * scritte dall'utente loggato.
     *
     * @param listaRecensioni la lista completa delle recensioni.
     * @return una nuova lista contenente solo le recensioni dell'utente.
     */
    private List<Recensione> filtraRecensioni(List<Recensione> listaRecensioni) {
        List<Recensione> nuovaLista = new ArrayList<>();

        for (Recensione r : listaRecensioni) {
            if (r.getIdUtente() == utenteLoggato.getId()) {
                Integer idRistorante = r.getIdRistorante();
                Ristorante ristorante = ristoranteCSV.cercaRistorante(idRistorante);
                r.setRistorante(ristorante);
                nuovaLista.add(r);
            }
        }

        return nuovaLista;
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Indietro".
     * Torna alla pagina principale dell'applicazione.
     */
    @FXML
    private void tornaIndietro() {
        SceneManager.apriPaginaPrincipale(stage);
    }
}