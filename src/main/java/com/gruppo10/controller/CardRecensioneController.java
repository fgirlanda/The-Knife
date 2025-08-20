/*
 * Francesco Girlanda 760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.classi.Card;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller per la card che visualizza una singola recensione. Implementa
 * l'interfaccia {@link Card} per gestire i dati di una recensione e visualizzarli
 * nell'interfaccia utente. Gestisce anche le azioni di risposta, modifica e rimozione
 * della recensione in base al ruolo dell'utente e al contesto.
 */
public class CardRecensioneController extends BasicController implements Card<Recensione> {

    private Recensione recensione;

    private Ristorante ristorante;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private VBox contenitore;

    private String titolo;

    private boolean profilo;

    @FXML
    private HBox card;

    @FXML
    private Text txtCliente;

    @FXML
    private Text txtTesto;

    @FXML
    private Text txtStelle;

    @FXML
    private Text txtRisposta;

    @FXML
    private Button btnRispondi;

    @FXML
    private Button btnRimuovi;

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
        titolo = stage.getTitle().toLowerCase();
        profilo = titolo.equals("the knife - profilo");
        if (utenteLoggato.getRuolo() == Ruolo.CLIENTE || utenteLoggato.getId() != this.ristorante.getIdproprietario()
                || !txtRisposta.getText().isBlank()) {
            btnRispondi.setVisible(false);
        }
        if (utenteLoggato.getId() != this.recensione.getIdUtente() || profilo) {
            btnRimuovi.setVisible(false);
            btnModifica.setVisible(false);
        }

        this.contenitore = contenitore;
    }

    /**
     * Imposta i dati della recensione sulle etichette della card.
     * I dati includono il nome del cliente, il testo, il voto in stelle
     * e l'eventuale risposta. Gestisce anche il caso in cui la card
     * si trovi all'interno della pagina del profilo.
     */
    @Override
    public void setDati() {
        if (profilo){
            txtCliente.setText(ristorante.getNomeRistorante());
            txtCliente.setOnMouseClicked(_ -> SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab));
        }
        else
            txtCliente.setText(this.recensione.getUsername());
        txtTesto.setText(this.recensione.getTesto());
        txtStelle.setText("★".repeat(this.recensione.getStelle()));
        txtRisposta.setText(this.recensione.getRisposta());
    }

    /**
     * Imposta il ristorante associato a questa recensione.
     *
     * @param ristorante l'oggetto {@link Ristorante} della recensione.
     */
    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

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
                    controller.setRecensione(this.recensione);
                    controller.setOnCloseCallBack(() -> {

                        String nuovaRisposta = controller.getRisposta();
                        if (nuovaRisposta != null) {
                            txtRisposta.setText(nuovaRisposta);
                            SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
                        }
                    });
                });
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Modifica".
     * Apre una finestra di dialogo per permettere al cliente di modificare
     * il testo e il voto della recensione.
     */
    @FXML
    private void apriModifica() {
        SceneManager.finestraDialogo("modifica_recensione.fxml", "Modifica", stage,
                (ModificaRecensioneController controller) -> {
                    controller.setStage(stage);
                    controller.setPrincipale(paginaPrincipale);
                    controller.setRecensione(this.recensione, this.contenitore);
                    controller.setRistorante(ristorante);
                });
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Rimuovi".
     * Rimuove la recensione dall'oggetto {@link Ristorante} e dal file CSV,
     * quindi aggiorna la pagina del ristorante per riflettere la modifica.
     */
    @FXML
    private void rimuovi() {
        this.ristorante.rimuoviRecensione(this.recensione);
        // RecensioneWriter.rimuoviRecensione(this.recensione);
        RecensioneCSV recensioneCSV = new RecensioneCSV();
        recensioneCSV.rimuovi(recensione);
        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
    }
}