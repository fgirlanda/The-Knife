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
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Controller per la vista del profilo cliente. Estende {@link BasicController}
 * e gestisce il caricamento e la visualizzazione dei dati utente, dei
 * ristoranti
 * preferiti e delle recensioni inserite dal cliente.
 */
public class ProfiloClienteController extends BasicController {

    /** Utente attualmente loggato */
    private Utente utenteLoggato = LoginController.utenteLoggato;

    /** Lista dei ristoranti caricati dal CSV */
    private List<Ristorante> ristoranti;

    /** Lista delle recensioni caricate dal CSV */
    private List<Recensione> recensioni;

    /** Gestore dei ristoranti tramite CSV */
    private RistoranteCSV ristoranteCSV = new RistoranteCSV();

    /** Gestore dei preferiti tramite CSV */
    private PreferitiCSV preferitiCSV = new PreferitiCSV();

    /** Contenitore per le tessere dei ristoranti preferiti */
    @FXML
    private VBox contenitoreTessereRis;

    /** Contenitore per le tessere delle recensioni dell'utente */
    @FXML
    private VBox contenitoreTessereRec;

    /**
     * Carica i dati del profilo dell'utente loggato.
     * Recupera e visualizza la lista dei ristoranti preferiti e delle recensioni
     * scritte dall'utente, popolando i rispettivi contenitori nella GUI.
     */
    public void caricaDati() {
        caricaDatiUtente();

        ristoranti = ristoranteCSV.caricaCSV();
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
            // recensioni = filtraRecensioni(ristoranti);
            SceneManager.caricaTessere(
                    recensioni,
                    contenitoreTessereRec,
                    stage,
                    "card_recensione.fxml",
                    (controller, _) -> {
                        ((CardRecensioneController) controller).setPrincipale(false);
                        ((CardRecensioneController) controller).setIndiceTab(2);
                    });

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
     * Filtra le liste di tutte le recensioni di ogni ristorante per trovare solo quelle
     * scritte dall'utente loggato.
     *
     * @param listaRecensioni la lista completa delle recensioni.
     * @return una nuova lista contenente solo le recensioni dell'utente.
     */
    private List<Recensione> filtraRecensioni(List<Ristorante> listaRistoranti) {
        List<Recensione> nuovaLista = new ArrayList<>();

        for (Ristorante ris : listaRistoranti) {
            for (Recensione rec : ris.getRecensioni()) {
                if (rec.getIdUtente() == utenteLoggato.getId()) {
                    nuovaLista.add(rec);
                }
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