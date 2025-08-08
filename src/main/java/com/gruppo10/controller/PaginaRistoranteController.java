package com.gruppo10.controller;

import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.GestionePreferiti;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PaginaRistoranteController extends Controller {

    private Ristorante ristorante;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private List<Recensione> recensioni;

    private boolean presente = false;

    @FXML
    private Label txtIndirizzo;

    @FXML
    private Label txtPaese;

    @FXML
    private Label txtMediaRec;

    @FXML
    private Label txtPrezzo;

    @FXML
    private Label txtNomeRistorante;

    @FXML
    private Text txtDescrizione;

    @FXML
    private ImageView imagePreferiti;

    @FXML
    private Button btnIndietro;

    @FXML
    private Button btnAggiungiRecensione;

    @FXML
    private Button btnPreferiti;

    @FXML
    private VBox contenitoreTessere;


    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;

        if (GestionePreferiti.controlloPreferito(utenteLoggato.getId(), ristorante.getId())) {
            imagePreferiti.setImage(new ImageView("/images/cuore_pieno.png").getImage());
        } else {
            imagePreferiti.setImage(new ImageView("/images/cuore_vuoto.png").getImage());
        }

        RecensioneCSV recensioneCSV = new RecensioneCSV();
        recensioni = recensioneCSV.caricaCSV();
        if (recensioni != null) {
            List<Recensione> listaFiltrata = filtraRecensioni(recensioni);
            presente = recensioneInserita(listaFiltrata);
            SceneManager.caricaTessere(
                    listaFiltrata,
                    contenitoreTessere,
                    stage,
                    "/GUI/card_recensione.fxml",
                    (controller, _) -> {
                        ((CardRecensioneController) controller).setRistorante(this.ristorante);
                        ((CardRecensioneController) controller).setPrincipale(paginaPrincipale);
                    });
        }

        if (utenteLoggato.getRuolo() == Ruolo.RISTORATORE || utenteLoggato.getRuolo() == Ruolo.NON_REGISTRATO) {
            btnAggiungiRecensione.setVisible(false);
            btnPreferiti.setVisible(false);
        }

        if (presente) {
            btnAggiungiRecensione.setVisible(false);
        }
    }

    public void setDati() {
        int numRecensioni = this.ristorante.getNumeroRecensioni();
        String[] indirizzo = this.ristorante.getIndirizzo().split(",");
        txtIndirizzo.setText(indirizzo[0]);
        txtPaese.setText(indirizzo[1]);
        txtMediaRec.setText(this.ristorante.getMediaRec().toString() + " (" + numRecensioni + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo());
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtDescrizione.setText(this.ristorante.getDescrizione());
    }

    @FXML
    private void aggiungiRecensione() {
        SceneManager.finestraDialogo("/GUI/aggiungi_recensione.fxml", "Aggiungi Recensione", stage,
                (AggiungiRecensioneController controller) -> {
                    controller.setRistorante(this.ristorante);
                    controller.setStage(stage);
                    controller.setPrincipale(paginaPrincipale);
                });
    }

    @FXML
    private void tornaIndietro() {
        if (paginaPrincipale) {
            SceneManager.apriPaginaPrincipale(stage);
        } else {
            if (utenteLoggato.getRuolo().equals(Ruolo.CLIENTE)) {
                SceneManager.apriProfilo(stage, 1);
            } else {
                SceneManager.apriProfiloRistoratore(stage, 1);
            }
        }
    }

    @FXML
    private void gestisciPreferiti() {
        if (imagePreferiti.getImage().getUrl().contains("cuore_vuoto.png")) {
            imagePreferiti.setImage(new ImageView("/images/cuore_pieno.png").getImage());
            aggiungiPreferito();
        } else {
            imagePreferiti.setImage(new ImageView("/images/cuore_vuoto.png").getImage());
            rimuoviPreferito();
        }

    }

    private void aggiungiPreferito() {
        if (!GestionePreferiti.controlloPreferito(utenteLoggato.getId(), this.ristorante.getId())) {
            GestionePreferiti.aggiungiPreferito(utenteLoggato.getId(), this.ristorante.getId());
        }
    }

    private void rimuoviPreferito() {
        GestionePreferiti.rimuoviPreferito(utenteLoggato.getId(), this.ristorante.getId());
    }

    private List<Recensione> filtraRecensioni(List<Recensione> recensioni) {
        List<Recensione> listaTemp = new ArrayList<>();
        for (Recensione r : recensioni) {
            if (r.getIdRis() == this.ristorante.getId()) {
                listaTemp.add(r);
            }
        }

        return listaTemp;
    }

    private boolean recensioneInserita(List<Recensione> recensioni) {
        for (Recensione rec : recensioni) {
            if (utenteLoggato.getId() == rec.getIdUtente()) {
                return true;
            }
        }
        return false;
    }
}