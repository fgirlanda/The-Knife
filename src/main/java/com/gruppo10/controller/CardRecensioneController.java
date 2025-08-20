/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
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

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

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

    @FXML
    private void rimuovi() {
        this.ristorante.rimuoviRecensione(this.recensione);
        // RecensioneWriter.rimuoviRecensione(this.recensione);
        RecensioneCSV recensioneCSV = new RecensioneCSV();
        recensioneCSV.rimuovi(recensione);
        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
    }
}