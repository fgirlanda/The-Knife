/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.util.HashMap;
import java.util.List;

import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.MediaRecensioni;

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

    /** Mappa che associa il ristorante alla sua distanza dall'utente loggato. */
    private HashMap<Ristorante, Double> mappaDistanze = new HashMap<>();

    /** Gestore dei ristoranti tramite file CSV. */
    private RistoranteCSV ristoranteCSV = new RistoranteCSV();
    
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
     * Carica tutti i ristoranti dal CSV e li visualizza nella pagina.
     * <p>
     * Aggiorna anche la mappa delle distanze dei ristoranti rispetto all'utente
     * loggato
     * e genera le card dei ristoranti nella vista.
     * </p>
     * <p>
     * Nota: se il caricamento genera errore, a {@code ristoranti} viene assegnato
     * {@code null}, {@link #caricaTessere(java.util.List)} e
     * {@link #mappaDistanze(java.util.List)} non vengono
     * eseguiti. L'errore è gestito da
     * {@link com.gruppo10.classi.GestioneEccezioni}, che notifica
     * l'utente con un popup.
     */
    public void setRistoranti() {
        ristoranti = ristoranteCSV.caricaCSV();
        if (ristoranti != null) {
            ristoranteCSV.creaMappa(ristoranti);
            caricaTessere(ristoranti);
            mappaDistanze(ristoranti);
        }
    }

    /**
     * Calcola la distanza tra l'utente loggato e ciascun ristorante.
     *
     * @param listaRistoranti Lista dei ristoranti di cui calcolare la distanza.
     */
    private void mappaDistanze(List<Ristorante> listaRistoranti) {
        for (Ristorante r : listaRistoranti) {
            Double dist = utenteLoggato.getCords().calcolaDistanza(r.getCords());
            mappaDistanze.put(r, dist);
        }
    }

    /**
     * Applica i filtri selezionati dall'utente e aggiorna la visualizzazione
     * delle card dei ristoranti.
     */
    @FXML
    public void applicaFiltri() {
        List<Ristorante> listaFiltrata = filtra(ristoranti);
        contenitoreTessere.getChildren().clear();
        caricaTessere(listaFiltrata);
    }

    /**
     * Filtra la lista di ristoranti in base ai criteri impostati nei ComboBox
     * e nel campo di ricerca.
     *
     * @param ristoranti Lista dei ristoranti da filtrare.
     * @return Lista filtrata dei ristoranti che soddisfano i criteri impostati.
     */
    private List<Ristorante> filtra(List<Ristorante> ristoranti) {
        String ricerca = ricercaField.getText().toLowerCase();
        TipoCucina filtroCucina = comboFiltroCucina.getValue();
        Prezzo filtroPrezzo = comboFiltroPrezzo.getValue();
        MediaRecensioni filtroRecensioni = comboFiltroRecensioni.getValue();
        Delivery filtroDelivery = comboFiltroDelivery.getValue();
        Prenotazione filtroPrenotazione = comboFiltroPrenotazione.getValue();
        Distanza filtroDistanza = comboFiltroDistanza.getValue();

        return ristoranti.stream()
                .filter(ristorante -> (ricerca.isBlank()
                        || ristorante.getNomeRistorante().toLowerCase().contains(ricerca)) &&
                        (filtroCucina == TipoCucina.TUTTO || ristorante.getTipoCucina() == filtroCucina) &&
                        (filtroPrezzo == Prezzo.TUTTO || ristorante.getPrezzo().getSoglia() <= filtroPrezzo.getSoglia())
                        &&
                        (filtroRecensioni == MediaRecensioni.TUTTO
                                || ristorante.getMediaRec() >= filtroRecensioni.getSoglia())
                        &&
                        (filtroDelivery == Delivery.TUTTO || ristorante.getDelivery() == filtroDelivery) &&
                        (filtroPrenotazione == Prenotazione.TUTTO || ristorante.getPrenotazione() == filtroPrenotazione)
                        &&
                        (mappaDistanze.get(ristorante) <= filtroDistanza.getKM()))
                .toList();
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
            SceneManager.apriProfiloRistoratore(stage, 0);
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
                (controller, _) -> {
                    ((CardRistoranteController) controller).setPrincipale(true);
                    ((CardRistoranteController) controller).setStage(stage);
                    ((CardRistoranteController) controller).setOnClick();
                });
    }
}
