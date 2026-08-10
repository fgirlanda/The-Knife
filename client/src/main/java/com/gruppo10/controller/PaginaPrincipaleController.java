/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.gui_elements.GestioneEccezioni;
import com.gruppo10.classi.MediaRecensioni;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.gui_elements.SceneManager;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controller per la pagina principale dell'applicazione.
 * <p>
 * Questa classe gestisce l'interfaccia utente che mostra l'elenco dei
 * ristoranti disponibili,
 * permette la ricerca e l'applicazione di filtri, e gestisce la navigazione
 * verso altre schermate
 * come il profilo utente. Inoltre, calcola le distanze dei ristoranti
 * rispetto all'utente loggato e aggiorna dinamicamente la visualizzazione delle
 * card.
 * </p>
 */
public class PaginaPrincipaleController extends BasicController {

    /** Lista di tutti i ristoranti caricati. */
    public List<Ristorante> ristoranti;

    /** Utente attualmente loggato nell'applicazione. */
    private Utente utenteLoggato = LoginController.utenteLoggato;
    
    /**
     * Pulsante per registrarsi o accedere al profilo, il cui testo cambia in base
     * al ruolo dell'utente.
     */
    @FXML
    private Button btnRegistratiProfilo;

    /** Contenitore verticale che ospita le card dei ristoranti. */
    @FXML
    private VBox contenitoreTessere;

    /** Campo di testo per inserire la ricerca per nome del ristorante. */
    @FXML
    private TextField ricercaField;

    /** Pulsante per avviare la ricerca applicando i filtri selezionati. */
    @FXML
    private Button btnCerca;

    /** ComboBox per filtrare i ristoranti in base al tipo di cucina. */
    @FXML
    private ComboBox<TipoCucina> comboFiltroCucina;

    /** ComboBox per filtrare i ristoranti in base alla fascia di prezzo. */
    @FXML
    private ComboBox<Prezzo> comboFiltroPrezzo;

    /** ComboBox per filtrare i ristoranti in base alla media delle recensioni. */
    @FXML
    private ComboBox<MediaRecensioni> comboFiltroRecensioni;

    /** ComboBox per filtrare i ristoranti che offrono servizio di delivery. */
    @FXML
    private ComboBox<Delivery> comboFiltroDelivery;

    /**
     * ComboBox per filtrare i ristoranti in base alla possibilità di prenotazione.
     */
    @FXML
    private ComboBox<Prenotazione> comboFiltroPrenotazione;

    /** ComboBox per filtrare i ristoranti in base alla distanza dall'utente. */
    @FXML
    private ComboBox<Distanza> comboFiltroDistanza;

    /**
     * Inizializza la pagina principale.
     * <p>
     * Imposta il testo del pulsante "Profilo/Registrati" in base al ruolo
     * dell'utente loggato,
     * popola tutti i ComboBox dei filtri con i valori possibili e imposta i valori
     * di default.
     * </p>
     */
    public void initialize() {
        if (utenteLoggato.getRuolo() == Ruolo.NON_REGISTRATO) {
            btnRegistratiProfilo.setText("Registrati");
        } else {
            btnRegistratiProfilo.setText("Profilo");
        }

        btnCerca.setDefaultButton(true);

        comboFiltroCucina.getItems().setAll(TipoCucina.values());
        comboFiltroPrezzo.getItems().setAll(Prezzo.values());
        comboFiltroRecensioni.getItems().setAll(MediaRecensioni.values());
        comboFiltroDistanza.getItems().setAll(Distanza.values());
        comboFiltroDelivery.getItems().setAll(Delivery.values());
        comboFiltroPrenotazione.getItems().setAll(Prenotazione.values());

        comboFiltroCucina.getSelectionModel().select(TipoCucina.TUTTO);
        comboFiltroPrezzo.getSelectionModel().select(Prezzo.TUTTO);
        comboFiltroRecensioni.getSelectionModel().select(MediaRecensioni.TUTTO);
        comboFiltroDelivery.getSelectionModel().select(Delivery.TUTTO);
        comboFiltroPrenotazione.getSelectionModel().select(Prenotazione.TUTTO);
        comboFiltroDistanza.getSelectionModel().select(Distanza.OLTRE);
    }

    /**
     * Carica tutti i ristoranti dal database e li visualizza nella pagina.
     */
    public void setRistoranti() {
        try {
            ristoranti = clientContext.getRistorantiService().getRistoranti();
            caricaTessere(ristoranti);
        } catch (RemoteException e) {
            GestioneEccezioni.errore("Errore di connessione al server", e, false, null);
        } catch (SQLException e) {
            GestioneEccezioni.errore("Errore durante il caricamento dei ristoranti", e, false, null);
        }
    }

    /**
     * Applica i filtri selezionati dall'utente e aggiorna la visualizzazione
     * delle card dei ristoranti.
     */
    @FXML
    public void applicaFiltri() {
        try {
            ristoranti = clientContext.getRistorantiService().cercaConFiltri(
                    ricercaField.getText(),
                    comboFiltroCucina.getValue(),
                    comboFiltroPrezzo.getValue(),
                    comboFiltroRecensioni.getValue(),
                    comboFiltroDelivery.getValue(),
                    comboFiltroPrenotazione.getValue(),
                    utenteLoggato.getCords(),
                    comboFiltroDistanza.getValue());
            contenitoreTessere.getChildren().clear();
            caricaTessere(ristoranti);
        } catch (IllegalArgumentException | SQLException | RemoteException e) {
            GestioneEccezioni.errore("Errore durante la ricerca dei ristoranti", e, false, null);
        }
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Profilo/Registrati".
     * <p>
     * In base al ruolo dell'utente loggato:
     * <ul>
     * <li>CLIENTE: apre la pagina del profilo cliente.</li>
     * <li>RISTORATORE: apre la pagina del profilo ristoratore.</li>
     * <li>NON_REGISTRATO: apre la pagina di registrazione.</li>
     * </ul>
     * </p>
     */
    @FXML
    private void gestisciBottoneUtente() {
        Ruolo ruolo = utenteLoggato.getRuolo();
        if (ruolo.equals(Ruolo.CLIENTE)) {
            SceneManager.apriProfilo(stage, 0);
        } else if (ruolo.equals(Ruolo.RISTORATORE)) {
            SceneManager.apriProfiloRistoratore(stage, 0, clientContext);
        } else {
            SceneManager.apriRegistrati(stage, true);
        }
    }

    /**
     * Carica le card dei ristoranti nella vista all'interno del VBox.
     *
     * @param lista Lista dei ristoranti da visualizzare.
     */
    private void caricaTessere(List<Ristorante> lista) {
        SceneManager.caricaTessere(
                lista,
                contenitoreTessere,
                stage,
                "card_ristorante.fxml",
                clientContext,
                (controller, _) -> {
                    ((CardRistoranteController) controller).setPrincipale(true);
                    ((CardRistoranteController) controller).setStage(stage);
                    ((CardRistoranteController) controller).setClientContext(clientContext);
                    ((CardRistoranteController) controller).setOnClick();
                });
    }
}
