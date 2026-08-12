/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.rmi.RemoteException;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;
import com.gruppo10.gui_elements.Card;
import com.gruppo10.gui_elements.GestioneEccezioni;
import com.gruppo10.gui_elements.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller per la card che visualizza una singola recensione. Implementa
 * l'interfaccia {@link Card} per gestire i dati di una recensione e
 * visualizzarli
 * nell'interfaccia grafica. Gestisce anche i bottoni di risposta, modifica e
 * rimozione
 * della recensione in base al ruolo dell'utente e al contesto.
 */
public class CardRecensioneController extends BasicController implements Card<Recensione> {

    /** L'utente attualmente loggato nell'applicazione. */
    private Utente utenteLoggato = LoginController.utenteLoggato;

    /** La recensione associata a questa card. */
    private Recensione recensione;

    /** Il ristorante a cui appartiene la recensione. */
    private Ristorante ristorante;

    /** Il contenitore {@link VBox} in cui la card è inserita. */
    private VBox contenitore;

    /**
     * Il titolo della finestra principale per determinare il contesto della card.
     */
    private String titolo;

    /** Flag che indica se la card è visualizzata nella pagina del profilo. */
    private boolean profilo;

    /** Contenitore principale della card (HBox). */
    @FXML
    private HBox card;

    /**
     * Testo che mostra il nome del cliente o del ristorante (a seconda del
     * contesto).
     */
    @FXML
    private Text txtCliente;

    /** Testo che mostra il contenuto della recensione. */
    @FXML
    private Text txtTesto;

    /** Testo che mostra il numero di stelle della recensione. */
    @FXML
    private Text txtStelle;

    /** Testo che mostra la risposta del proprietario alla recensione. */
    @FXML
    private Text txtRisposta;

    /** Pulsante per rispondere alla recensione. */
    @FXML
    private Button btnRispondi;

    /** Pulsante per rimuovere la recensione. */
    @FXML
    private Button btnRimuovi;

    /** Pulsante per modificare la recensione. */
    @FXML
    private Button btnModifica;

    /**
     * Imposta la recensione e il contenitore associati a questa card.
     * Configura la visibilità dei pulsanti di "Rispondi", "Rimuovi" e "Modifica"
     * in base al ruolo dell'utente loggato e al proprietario del ristorante.
     *
     * @param recensione  l'oggetto {@link Recensione} da visualizzare.
     * @param contenitore il {@link VBox} che contiene la card.
     */
    @Override
    public void setItem(Recensione recensione, VBox contenitore) {
        this.recensione = recensione;
        this.ristorante = recensione.getRistorante();
        this.contenitore = contenitore;
        titolo = stage.getTitle().toLowerCase();
        profilo = titolo.equals("the knife - profilo");
        if (utenteLoggato.getRuolo() == Ruolo.CLIENTE || utenteLoggato.getId() != ristorante.getIdproprietario()
                || !recensione.getRisposta().equals("")) {
            btnRispondi.setVisible(false);
        }
        if (utenteLoggato.getId() != this.recensione.getIdUtente() || profilo) {
            btnRimuovi.setVisible(false);
            btnModifica.setVisible(false);
        }

    }

    /**
     * Imposta i dati della recensione sulle etichette della card.
     * I dati includono il nome del cliente, il testo, il voto in stelle
     * e l'eventuale risposta. Gestisce anche il caso in cui la card
     * si trovi all'interno della pagina del profilo, per cui il Text
     * {@link #txtCliente} diventa il nome del ristorante, cliccabile per aprire la
     * pagina ristorante associata.
     */
    @Override
    public void setDati() {
        if (profilo) {
            txtCliente.setText(ristorante.getNomeRistorante());
            txtCliente.setOnMouseClicked(
                    _ -> SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab, clientContext));
        } else
            txtCliente.setText(recensione.getUsername());
        txtTesto.setText(recensione.getTesto());
        txtStelle.setText("★".repeat(recensione.getStelle()));
        txtRisposta.setText(recensione.getRisposta());
    }

    /**
     * Imposta il ristorante associato a questa recensione.
     *
     * @param ristorante l'oggetto {@link Ristorante} della recensione.
     */

    /**
     * Gestisce l'evento di clic sul pulsante "Rispondi".
     * Apre una finestra di dialogo per permettere al proprietario di un ristorante
     * di scrivere una risposta alla recensione. Aggiorna la card una volta che la
     * risposta è stata salvata.
     */
    @FXML
    private void rispondi() {
        SceneManager.finestraDialogo("rispondi_recensione.fxml", "Rispondi", stage,
                (RispostaRecensioneController controller) -> {
                    controller.setRecensione(recensione);
                    controller.setOnCloseCallBack(() -> {

                        String nuovaRisposta = controller.getRisposta();
                        if (nuovaRisposta != null) {
                            recensione.setRisposta(nuovaRisposta);
                            SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab, clientContext);
                        }
                    });
                });
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Modifica".
     * Apre una finestra di dialogo per permettere al cliente di modificare
     * il testo e/o il voto della recensione.
     */
    @FXML
    private void apriModifica() {
        SceneManager.finestraDialogo("modifica_recensione.fxml", "Modifica", stage,
                (ModificaRecensioneController controller) -> {
                    controller.setStage(stage);
                    controller.setClientContext(clientContext);
                    controller.setPrincipale(paginaPrincipale);
                    controller.setRecensione(recensione, contenitore);
                    controller.setRistorante(ristorante);
                });
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Rimuovi".
     * Rimuove la recensione dal database e poi dall'oggetto {@link Ristorante},
     * quindi aggiorna la pagina del ristorante per riflettere la modifica.
     */
    @FXML
    private void rimuovi() {
        try {
            clientContext.getRecensioniService().rimuoviRecensione(recensione, ristorante);
        } catch (RemoteException e) {
            GestioneEccezioni.errore("Errore di connessione al server", e, false, null);
            return;
        }
        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab, clientContext);
    }
}
