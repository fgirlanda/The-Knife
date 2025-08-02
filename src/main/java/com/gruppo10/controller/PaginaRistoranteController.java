package com.gruppo10.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.PreferitiReader;
import com.gruppo10.classi.PreferitiWriter;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneReader;
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
import javafx.stage.Stage;

public class PaginaRistoranteController {

    private Stage stage;

    private Ristorante ristorante;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private boolean paginaPrincipale; // true -> arrivo dalla pagina principale, false -> arrivo dal profilo

    private List<Recensione> recensioni;

    private boolean presente = false;

    @FXML private Label txtIndirizzo;

    @FXML private Label txtPaese;

    @FXML private Label txtMediaRec;

    @FXML private Label txtPrezzo;

    @FXML private Label txtNomeRistorante;

    @FXML private Text txtDescrizione;

    @FXML private ImageView imagePreferiti;

    @FXML private Button btnIndietro;

    @FXML private Button btnAggiungiRecensione;

    @FXML private Button btnPreferiti;

    @FXML private VBox contenitoreTessere;

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;

        if (PreferitiReader.controlloPreferito(utenteLoggato.getId(), ristorante.getId())) {
            imagePreferiti.setImage(new ImageView("/images/cuore_pieno.png").getImage());
        } else {
            imagePreferiti.setImage(new ImageView("/images/cuore_vuoto.png").getImage());
        }
        
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "recensioni.csv");
        File fileRecensioni = new File(path.toString());
        if (fileRecensioni.exists()) {
            recensioni = RecensioneReader.caricaCSV(path.toString());
            List<Recensione> listaFiltrata = filtraRecensioni(recensioni);
            presente = recensioneInserita(listaFiltrata);
            SceneManager.caricaTessere(
                listaFiltrata,
                contenitoreTessere,
                stage,
                "/GUI/card_recensione.fxml",
                (controller, _) -> {
                    ((CardRecensioneController) controller).setIdProprietario(this.ristorante.getIdproprietario());
                }
            );
        }

        if (utenteLoggato.getRuolo() == Ruolo.RISTORATORE || utenteLoggato.getRuolo() == Ruolo.NON_REGISTRATO) {
            btnAggiungiRecensione.setVisible(false);
            btnPreferiti.setVisible(false);
        }

        if(presente){
            btnAggiungiRecensione.setVisible(false);
        }
    }


    public void setDati() {
        String[] indirizzo = this.ristorante.getIndirizzo().split(",");
        txtIndirizzo.setText(indirizzo[0]);
        txtPaese.setText(indirizzo[1]);
        txtMediaRec.setText(this.ristorante.getMediaRec().toString());
        txtPrezzo.setText(this.ristorante.getPrezzo());
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtDescrizione.setText(this.ristorante.getDescrizione());
    }


    @FXML
    private void aggiungiRecensione() {
        SceneManager.finestraDialogo("/GUI/aggiungi_recensione.fxml", "Aggiungi Recensione", stage,
                (AggiungiRecensioneController controller) -> {
                    controller.setStage(stage);
                    controller.setRistorante(this.ristorante);
                });
    }


    @FXML
    private void tornaIndietro() {
        if (paginaPrincipale) {
            SceneManager.tornaPaginaPrincipale(stage);
        } else {
            SceneManager.tornaProfilo(stage, utenteLoggato);
        }
    }


    @FXML
    private void gestisciPreferiti() {
        try {
            if (imagePreferiti.getImage().getUrl().contains("cuore_vuoto.png")) {
                imagePreferiti.setImage(new ImageView("/images/cuore_pieno.png").getImage());
                aggiungiPreferito();
            } else {
                imagePreferiti.setImage(new ImageView("/images/cuore_vuoto.png").getImage());
                rimuoviPreferito();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void aggiungiPreferito() {
        try {
            if (!PreferitiReader.controlloPreferito(utenteLoggato.getId(), this.ristorante.getId())) {
                PreferitiWriter.aggiungiPreferito(utenteLoggato.getId(), this.ristorante.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void rimuoviPreferito() {
        try {
            PreferitiWriter.rimuoviPreferito(utenteLoggato.getId(), this.ristorante.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        for(Recensione rec: recensioni){
            if(utenteLoggato.getId() == rec.getIdUtente()){
                return true;
            }
        }
        return false;
    }
}