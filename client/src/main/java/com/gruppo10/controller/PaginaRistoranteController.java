/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.TheKnife;
import com.gruppo10.classi.GestioneEccezioni;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.database.PreferitoDAO;
import com.gruppo10.database.RecensioneDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller per la pagina del singolo ristorante.
 * Gestisce la visualizzazione dei dettagli del ristorante, delle recensioni,
 * l'aggiunta e la gestione delle recensioni e l'aggiunta del ristorante ai
 * preferiti.
 */
public class PaginaRistoranteController extends BasicController {

    /** Ristorante corrente visualizzato nella pagina. */
    private Ristorante ristorante;

    /** Utente attualmente loggato. */
    private Utente utenteLoggato = LoginController.utenteLoggato;

    /** Lista di recensioni associate al ristorante corrente. */
    private List<Recensione> recensioni;

    /**
     * Flag che indica se l'utente ha già inserito una recensione per questo
     * ristorante.
     */
    private boolean recPresente = false;

    /** Gestore dei preferiti persistiti nel database. */
    private final PreferitoDAO preferitoDAO = new PreferitoDAO();

    /** Percorso dell'immagine del cuore pieno per i preferiti. */
    private URL cuorePienoURL = TheKnife.class.getResource("/images/cuore_pieno.png");

    /** Percorso dell'immagine del cuore vuoto per i preferiti. */
    private URL cuoreVuotoURL = TheKnife.class.getResource("/images/cuore_vuoto.png");

    /** Label che mostra l'indirizzo del ristorante. */
    @FXML
    private Label txtIndirizzo;

    /** Label che mostra il paese del ristorante. */
    @FXML
    private Label txtPaese;

    /** Label che mostra la media delle recensioni del ristorante. */
    @FXML
    private Label txtMediaRec;

    /** Label che mostra la fascia di prezzo del ristorante. */
    @FXML
    private Label txtPrezzo;

    /** Label che mostra il nome del ristorante. */
    @FXML
    private Label txtNomeRistorante;

    /** Testo che mostra la descrizione del ristorante. */
    @FXML
    private Text txtDescrizione;

    /**
     * Immagine che indica se il ristorante è tra i preferiti dell'utente loggato o meno (cuore pieno o
     * vuoto).
     */
    @FXML
    private ImageView imagePreferiti;

    /** Immagine del ristorante. */
    @FXML
    private ImageView imgRistorante;

    /** Pulsante per tornare alla schermata precedente. */
    @FXML
    private Button btnIndietro;

    /** Pulsante per aggiungere una nuova recensione. */
    @FXML
    private Button btnAggiungiRecensione;

    /** Pulsante per aggiungere o rimuovere il ristorante dai preferiti. */
    @FXML
    private Button btnPreferiti;

    /** Contenitore per le tessere delle recensioni. */
    @FXML
    private VBox contenitoreTessere;

    /**
     * Imposta il ristorante da visualizzare nella pagina.
     * Carica l'immagine del ristorante, controlla se è tra i preferiti
     * dell'utente e carica le recensioni associate, nascondendo i pulsanti
     * non pertinenti in base al ruolo dell'utente e alla presenza di una
     * recensione.
     *
     * @param ristorante l'oggetto {@link Ristorante} da visualizzare.
     */
    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;

        String cucina = this.ristorante.getTipoCucina().name();
        URL imgURL = TheKnife.class.getResource("/images/"+cucina+".png");
        Image immagine = new Image(imgURL.toExternalForm());
        imgRistorante.setImage(immagine);

        imagePreferiti.setImage(new ImageView(cuoreVuotoURL.toExternalForm()).getImage());

        recensioni = ristorante.getRecensioni();
        if (recensioni != null) {
            SceneManager.caricaTessere(
                    recensioni,
                    contenitoreTessere,
                    stage,
                    "card_recensione.fxml",
                    (controller, _) -> {
                        ((CardRecensioneController) controller).setPrincipale(paginaPrincipale);
                        ((CardRecensioneController) controller).setIndiceTab(indiceTab);
                    });
        }

        if (utenteLoggato.getRuolo() == Ruolo.RISTORATORE || utenteLoggato.getRuolo() == Ruolo.NON_REGISTRATO) {
            btnAggiungiRecensione.setVisible(false);
            btnPreferiti.setVisible(false);
        } else {
            try {
                boolean preferito = preferitoDAO.controlloPreferito(
                        utenteLoggato.getId(), ristorante.getId());
                imagePreferiti.setImage(new ImageView((preferito ? cuorePienoURL : cuoreVuotoURL)
                        .toExternalForm()).getImage());
                recPresente = new RecensioneDAO().esisteRecensione(
                        utenteLoggato.getId(), ristorante.getId());
                if (recPresente) {
                    btnAggiungiRecensione.setVisible(false);
                }
            } catch (IllegalArgumentException | SQLException e) {
                GestioneEccezioni.errore("Errore durante il caricamento dei dati del ristorante", e, false, null);
            }
        }
    }

    /**
     * Imposta i dati del ristorante nelle etichette dell'interfaccia utente.
     * I dati includono nome, indirizzo, media delle recensioni, prezzo e
     * descrizione.
     */
    public void setDati() {
        int numRecensioni = this.ristorante.getNumeroRecensioni();
        String[] indirizzo = this.ristorante.getIndirizzo().split(",");
        txtIndirizzo.setText(indirizzo[0]);
        txtPaese.setText(indirizzo[1]);
        txtMediaRec.setText(
                String.format("%.1f", this.ristorante.getMediaRec()) + " (" + numRecensioni + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo().toString());
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtDescrizione.setText(this.ristorante.getDescrizione());
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Aggiungi Recensione".
     * Apre una nuova finestra di dialogo per permettere all'utente di scrivere
     * una recensione.
     */
    @FXML
    private void aggiungiRecensione() {
        SceneManager.finestraDialogo("aggiungi_recensione.fxml", "Aggiungi Recensione", stage,
                (AggiungiRecensioneController controller) -> {
                    controller.setRistorante(this.ristorante);
                    controller.setStage(stage);
                    controller.setPrincipale(paginaPrincipale);
                });
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Indietro".
     * Torna alla pagina precedente (pagina principale, profilo cliente o profilo
     * ristoratore)
     * in base al flag {@code paginaPrincipale} e al ruolo dell'utente.
     */
    @FXML
    private void tornaIndietro() {
        if (paginaPrincipale) {
            SceneManager.apriPaginaPrincipale(stage);
        } else {
            if (utenteLoggato.getRuolo().equals(Ruolo.CLIENTE)) {
                SceneManager.apriProfilo(stage, indiceTab);
            } else {
                SceneManager.apriProfiloRistoratore(stage, indiceTab);
            }
        }
    }

    /**
     * Gestisce l'evento di clic sul pulsante dei preferiti.
     * Aggiunge o rimuove il ristorante dalla lista dei preferiti dell'utente
     * e aggiorna l'immagine del cuore di conseguenza.
     */
    @FXML
    private void gestisciPreferiti() {
        int idUt = utenteLoggato.getIdUtente();
        int idRis = this.ristorante.getIdRistorante();
        boolean daAggiungere = imagePreferiti.getImage().getUrl().contains("cuore_vuoto.png");
        try {
            if (daAggiungere) {
                preferitoDAO.aggiungiPreferito(idUt, idRis);
                imagePreferiti.setImage(new ImageView(cuorePienoURL.toExternalForm()).getImage());
            } else {
                preferitoDAO.rimuoviPreferito(idUt, idRis);
                imagePreferiti.setImage(new ImageView(cuoreVuotoURL.toExternalForm()).getImage());
            }
        } catch (IllegalArgumentException | SQLException e) {
            GestioneEccezioni.errore("Errore durante l'aggiornamento dei preferiti", e, false, null);
        }
    }
}
