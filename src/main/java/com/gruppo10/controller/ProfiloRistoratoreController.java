/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Controller per la vista del profilo del ristoratore. Estende {@link BasicController}
 * e gestisce il caricamento e la visualizzazione dei dati utente e dei ristoranti
 * di proprietà del ristoratore loggato. Permette inoltre di aggiungere nuovi ristoranti
 * e di tornare alla pagina principale.
 */
public class ProfiloRistoratoreController extends BasicController{

    private Utente utenteloggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Ristorante> listaFiltrata;

    @FXML
    private VBox contenitoreTessere;

    /**
     * Carica i dati del profilo del ristoratore loggato.
     * Recupera e visualizza la lista dei ristoranti posseduti dall'utente,
     * aggiornando il contenitore delle tessere.
     */
    public void caricaDati(){
        caricaDatiUtente();
        RistoranteCSV ristoranteCSV = new RistoranteCSV();
        ristoranti = ristoranteCSV.caricaCSV();
        if (ristoranti != null) {
            listaFiltrata = filtraProprietario(ristoranti);
            aggiornaContenitore(listaFiltrata);
        }
    }

    /**
     * Filtra la lista di tutti i ristoranti per trovare solo quelli
     * che sono di proprietà del ristoratore loggato.
     *
     * @param listaRistoranti la lista completa dei ristoranti.
     * @return una nuova lista contenente solo i ristoranti di proprietà dell'utente.
     */
    private List<Ristorante> filtraProprietario(List<Ristorante> listaRistoranti) {
        List<Ristorante> nuovaLista = new ArrayList<>();

        for (Ristorante r : listaRistoranti) {
            if (r.getIdproprietario() == utenteloggato.getId()) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }

    /**
     * Gestisce l'evento di clic sul pulsante per aggiungere un nuovo ristorante.
     * Apre una finestra di dialogo per l'inserimento dei dati del nuovo ristorante.
     * Se l'operazione ha successo, aggiorna la lista dei ristoranti visualizzati.
     */
    @FXML
    private void apriAggiungiRistorante() {
        SceneManager.finestraDialogo("aggiungi_ristorante.fxml", "Aggiungi Ristorante", stage,
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

    /**
     * Gestisce l'evento di clic sul pulsante "Indietro".
     * Torna alla pagina principale dell'applicazione.
     */
    @FXML
    public void tornaIndietro() {
        SceneManager.apriPaginaPrincipale(stage);
    }

    /**
     * Aggiorna la vista del contenitore delle tessere con la lista di ristoranti fornita.
     *
     * @param lista la lista di {@link Ristorante} da visualizzare.
     */
    private void aggiornaContenitore(List<Ristorante> lista) {
        SceneManager.caricaTessere(
                lista,
                contenitoreTessere,
                stage,
                "card_ristorante.fxml",
                (controller, _) -> {
                    ((CardRistoranteController) controller).setPrincipale(false);
                    ((CardRistoranteController) controller).setStage(stage);
                    ((CardRistoranteController) controller).setOnClick();
                    ((CardRistoranteController) controller).setIndiceTab(1);
                });
    }
}