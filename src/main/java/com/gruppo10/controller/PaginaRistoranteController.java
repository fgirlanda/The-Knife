/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.PreferitiCSV;
import com.gruppo10.classi.Preferito;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PaginaRistoranteController extends BasicController {

    private Ristorante ristorante;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private List<Recensione> recensioni;

    private boolean presente = false;

    private PreferitiCSV preferitiCSV = new PreferitiCSV();

    private String pathCuorePieno = Paths.get("images","cuore_pieno.png").toString();

    private String pathCuoreVuoto = Paths.get("images","cuore_vuoto.png").toString();

    private int indiceTab;

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
    private ImageView imgRistorante;

    @FXML
    private Button btnIndietro;

    @FXML
    private Button btnAggiungiRecensione;

    @FXML
    private Button btnPreferiti;

    @FXML
    private VBox contenitoreTessere;

    public void setIndiceTab(int indiceTab){
        this.indiceTab = indiceTab;
    }
    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;

        String cucina = this.ristorante.getTipoCucina().toString();
        String path = Paths.get("images",cucina+".png").toString();
        Image immagine = new Image(path);
        imgRistorante.setImage(immagine);

        if (preferitiCSV.controlloPreferito(utenteLoggato.getIdUtente(), ristorante.getIdRistorante())) {
            imagePreferiti.setImage(new ImageView(pathCuorePieno).getImage());
        } else {
            imagePreferiti.setImage(new ImageView(pathCuoreVuoto).getImage());
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
                    "card_recensione.fxml",
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
        txtMediaRec.setText(String.format("%.1f", this.ristorante.getMediaRec()) + " (" + numRecensioni + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo().toString());
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtDescrizione.setText(this.ristorante.getDescrizione());
    }

    @FXML
    private void aggiungiRecensione() {
        SceneManager.finestraDialogo("aggiungi_recensione.fxml", "Aggiungi Recensione", stage,
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
                SceneManager.apriProfilo(stage, indiceTab);
            } else {
                SceneManager.apriProfiloRistoratore(stage, indiceTab);
            }
        }
    }

    @FXML
    private void gestisciPreferiti() {
        if (imagePreferiti.getImage().getUrl().contains("cuore_vuoto.png")) {
            imagePreferiti.setImage(new ImageView(pathCuorePieno).getImage());
            aggiungiPreferito();
        } else {
            imagePreferiti.setImage(new ImageView(pathCuoreVuoto).getImage());
            rimuoviPreferito();
        }

    }

    private void aggiungiPreferito() {
        int idUt = utenteLoggato.getIdUtente();
        int idRis = this.ristorante.getIdRistorante();
        if (!preferitiCSV.controlloPreferito(idUt, idRis)) {
            Preferito preferito = new Preferito(idUt, idRis);
            preferitiCSV.scrivi(preferito);
        }
    }

    private void rimuoviPreferito() {
        Preferito preferito = new Preferito(utenteLoggato.getIdUtente(), this.ristorante.getIdRistorante());
        preferitiCSV.rimuovi(preferito);
    }

    private List<Recensione> filtraRecensioni(List<Recensione> recensioni) {
        List<Recensione> listaTemp = new ArrayList<>();
        for (Recensione r : recensioni) {
            if (r.getIdRistorante() == this.ristorante.getId()) {
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