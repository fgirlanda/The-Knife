/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.gui_elements.GestioneEccezioni;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.gui_elements.SceneManager;
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

    /** Lista dei ristoranti preferiti caricati dal database. */
    private List<Ristorante> ristoranti;

    /** Lista delle recensioni dell'utente caricate dal database. */
    private List<Recensione> recensioni;

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

        try {
            ristoranti = clientContext.getRistorantiService().trovaPreferitiPerUtente(utenteLoggato.getId());
            SceneManager.caricaTessere(
                    ristoranti,
                    contenitoreTessereRis,
                    stage,
                    "card_ristorante.fxml",
                    clientContext,
                    (controller, _) -> {
                        ((CardRistoranteController) controller).setPrincipale(false);
                        ((CardRistoranteController) controller).setStage(stage);
                        ((CardRistoranteController) controller).setClientContext(clientContext);
                        ((CardRistoranteController) controller).setOnClick();
                        ((CardRistoranteController) controller).setIndiceTab(1);
                    });
            recensioni = clientContext.getRecensioniService().trovaPerUtenteConRistorante(utenteLoggato.getId());
            SceneManager.caricaTessere(
                    recensioni,
                    contenitoreTessereRec,
                    stage,
                    "card_recensione.fxml",
                    clientContext,
                    (controller, _) -> {
                        ((CardRecensioneController) controller).setPrincipale(false);
                        ((CardRecensioneController) controller).setClientContext(clientContext);
                        ((CardRecensioneController) controller).setIndiceTab(2);
                    });

        } catch (IllegalArgumentException | SQLException e) {
            GestioneEccezioni.errore("Errore durante il caricamento del profilo", e, false, null);
        } catch (RemoteException e) {
            GestioneEccezioni.errore("Errore di connessione al server", e, false, null);
        }
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
