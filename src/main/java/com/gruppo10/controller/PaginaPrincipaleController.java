/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.util.HashMap;
import java.util.List;

import com.gruppo10.classi.FiltroPrezzo;
import com.gruppo10.classi.FiltroTipoCucina;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.FiltroDelivery;
import com.gruppo10.classi.FiltroDistanza;
import com.gruppo10.classi.FiltroPrenotazione;
import com.gruppo10.classi.FiltroMediaRecensioni;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PaginaPrincipaleController extends Controller {

    public static List<Ristorante> ristoranti;

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private HashMap<String, Double> mappaDistanze = new HashMap<>();

    @FXML
    private Button bottoneRegistratiProfilo;

    @FXML
    private VBox contenitoreTessere;

    @FXML
    private TextField txtRicerca;

    @FXML
    private Button btnCerca;

    @FXML
    private ComboBox<FiltroTipoCucina> comboFiltroCucina;

    @FXML
    private ComboBox<FiltroPrezzo> comboFiltroPrezzo;

    @FXML
    private ComboBox<FiltroMediaRecensioni> comboFiltroRecensioni;

    @FXML
    private ComboBox<FiltroDelivery> comboFiltroDelivery;

    @FXML
    private ComboBox<FiltroPrenotazione> comboFiltroPrenotazione;

    @FXML
    private ComboBox<FiltroDistanza> comboFiltroDistanza;

    public void initialize() {
        if (utenteLoggato.getRuolo() == Ruolo.NON_REGISTRATO) {
            bottoneRegistratiProfilo.setText("Registrati");
        } else {
            bottoneRegistratiProfilo.setText("Profilo");
        }

        comboFiltroCucina.getItems().setAll(FiltroTipoCucina.values());
        comboFiltroPrezzo.getItems().setAll(FiltroPrezzo.values());
        comboFiltroRecensioni.getItems().setAll(FiltroMediaRecensioni.values());
        comboFiltroDistanza.getItems().setAll(FiltroDistanza.values());
        comboFiltroDelivery.getItems().setAll(FiltroDelivery.values());
        comboFiltroPrenotazione.getItems().setAll(FiltroPrenotazione.values());
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
        String ricerca = txtRicerca.getText().toLowerCase();
        String filtroCucina = comboFiltroCucina.getValue() != null
                && !comboFiltroCucina.getValue().toString().equals("TUTTO") ? comboFiltroCucina.getValue().toString()
                        : "";
        String filtroPrezzo = comboFiltroPrezzo.getValue() != null
                && !comboFiltroPrezzo.getValue().toString().equals("TUTTO") ? comboFiltroPrezzo.getValue().toString()
                        : "";
        String filtroRecensioni = comboFiltroRecensioni.getValue() != null
                && !comboFiltroRecensioni.getValue().toString().equals("TUTTO")
                        ? comboFiltroRecensioni.getValue().toString()
                        : "";
        String filtroDelivery = comboFiltroDelivery.getValue() != null
                && !comboFiltroDelivery.getValue().toString().equals("TUTTO")
                        ? comboFiltroDelivery.getValue().toString()
                        : "";
        String filtroPrenotazione = comboFiltroPrenotazione.getValue() != null
                && !comboFiltroPrenotazione.getValue().toString().equals("TUTTO")
                        ? comboFiltroPrenotazione.getValue().toString()
                        : "";

        Double filtroDistanza = comboFiltroDistanza.getValue() != null
                && !comboFiltroDistanza.getValue().toString().equals("50+ km") ? comboFiltroDistanza.getValue().getKM()
                        : Double.MAX_VALUE;

        // Separare estrazione dati da controlli

        return ristoranti.stream().filter(
                ristorante -> (ricerca.isEmpty() || ristorante.getNomeRistorante().toLowerCase().contains(ricerca)) &&
                        (filtroPrezzo.isEmpty() || ristorante.getPrezzo().equals(filtroPrezzo)) &&
                        (filtroCucina.isEmpty() || ristorante.getTipoCucina().name().equals(filtroCucina)) &&
                        (filtroRecensioni.isEmpty() || ristorante.getMediaRec() >= filtroRecensioni.length()) &&
                        (filtroDelivery.isEmpty() || (filtroDelivery.equals("DELIVERY_DISPONIBILE") &&

                                ristorante.isDelivery())
                                || (filtroDelivery.equals("DELIVERY_NON_DISPONIBILE") && !ristorante.isDelivery()))
                        &&
                        (filtroPrenotazione.isEmpty()
                                || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_DISPONIBILE")
                                        && ristorante.isPrenotazioneOnline())
                                || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_NON_DISPONIBILE")
                                        && !ristorante.isPrenotazioneOnline()))
                        &&
                        (mappaDistanze.get(ristorante.getNomeRistorante()) <= filtroDistanza))
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
