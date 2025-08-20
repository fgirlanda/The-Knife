/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
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

public class PaginaPrincipaleController extends BasicController {

    public static List<Ristorante> ristoranti;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private HashMap<String, Double> mappaDistanze = new HashMap<>();

    @FXML
    private Button btnRegistratiProfilo;

    @FXML
    private VBox contenitoreTessere;

    @FXML
    private TextField ricercaField;

    @FXML
    private Button btnCerca;

    @FXML
    private ComboBox<TipoCucina> comboFiltroCucina;

    @FXML
    private ComboBox<Prezzo> comboFiltroPrezzo;

    @FXML
    private ComboBox<MediaRecensioni> comboFiltroRecensioni;

    @FXML
    private ComboBox<Delivery> comboFiltroDelivery;

    @FXML
    private ComboBox<Prenotazione> comboFiltroPrenotazione;

    @FXML
    private ComboBox<Distanza> comboFiltroDistanza;

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

    // Lista ristoranti, calcolo distanze e caricamento card
    public void setRistoranti() {
        RistoranteCSV ristoranteCSV = new RistoranteCSV();
        ristoranti = ristoranteCSV.caricaCSV();
        if (ristoranti != null) {
            caricaTessere(ristoranti);
            mappaDistanze(ristoranti);
        }
    }

    // Calcola distanze per ogni ristorante
    private void mappaDistanze(List<Ristorante> listaRistoranti) {
        for (Ristorante r : listaRistoranti) {
            Double dist = utenteLoggato.getCords().calcolaDistanza(r.getCords());
            mappaDistanze.put(r.getNomeRistorante(), dist);
        }
    }

    @FXML
    public void applicaFiltri() {
        List<Ristorante> listaFiltrata = filtra(ristoranti);
        contenitoreTessere.getChildren().clear();
        caricaTessere(listaFiltrata);
    }

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
                        (mappaDistanze.get(ristorante.getNomeRistorante()) <= filtroDistanza.getKM()))
                .toList();
    }

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
