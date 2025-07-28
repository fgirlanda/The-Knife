package com.gruppo10.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import com.gruppo10.classi.RistoranteReader;
import com.gruppo10.classi.FiltroPrezzo;
import com.gruppo10.classi.FiltroTipoCucina;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneReader;
import com.gruppo10.classi.Ristorante;
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
import javafx.stage.Stage;

public class PaginaPrincipaleController {

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private Stage stage;
    @FXML private Button bottoneRegistratiProfilo;

    @FXML private VBox contenitoreTessere;

    @FXML private TextField txtRicerca;

    @FXML private Button btnCerca;

    @FXML private ComboBox<FiltroTipoCucina> comboFiltroCucina;
    
    @FXML private ComboBox<FiltroPrezzo> comboFiltroPrezzo;

    @FXML private ComboBox<FiltroMediaRecensioni> comboFiltroRecensioni;

    @FXML private ComboBox<FiltroDelivery> comboFiltroDelivery;

    @FXML private ComboBox<FiltroPrenotazione> comboFiltroPrenotazione;

    @FXML private ComboBox<FiltroDistanza> comboFiltroDistanza;

    public static List<Ristorante> ristoranti; 

    public static List<Recensione> recensioni = RecensioneReader.caricaCSV("fileCSV/recensioni.csv");

    private HashMap<String, Double> mappaDistanze = new HashMap<>();


    public void initialize() {
        // Tasto registrati-profilo
        if (utenteLoggato.getRuolo() == Ruolo.NON_REGISTRATO) {
            bottoneRegistratiProfilo.setText("Registrati");
        } else {
            bottoneRegistratiProfilo.setText("Profilo");
        }

        // Filtri
        comboFiltroCucina.getItems().setAll(FiltroTipoCucina.values());
        comboFiltroPrezzo.getItems().setAll(FiltroPrezzo.values());
        comboFiltroRecensioni.getItems().setAll(FiltroMediaRecensioni.values());
        comboFiltroDistanza.getItems().setAll(FiltroDistanza.values());
        comboFiltroDelivery.getItems().setAll(FiltroDelivery.values());
        comboFiltroPrenotazione.getItems().setAll(FiltroPrenotazione.values());
    }
    
    
    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
        // Caricamento schede ristorante
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti.csv");
        ristoranti = RistoranteReader.caricaCSV(path.toString());
        mappaDistanze(ristoranti);
        RistoranteReader.caricaTessere(ristoranti, contenitoreTessere, stage, true);
    }


    // Calcola distanze per ogni ristorante
    private void mappaDistanze(List<Ristorante> listaRistoranti){
        for(Ristorante r: listaRistoranti){
            Double dist = utenteLoggato.getCords().calcolaDistanza(r.getCords());
            mappaDistanze.put(r.getNomeRistorante(), dist);
        }
    }


    @FXML
    public void ricercaRistorante() {
        contenitoreTessere.getChildren().clear(); // Pulisce il contenitore prima di aggiungere i risultati
        List<Ristorante> listaFiltrata = filtra(ristoranti); 
        RistoranteReader.caricaTessere(listaFiltrata, contenitoreTessere, stage, true);
    }


    private List<Ristorante> filtra(List<Ristorante> ristoranti){
        String ricerca = txtRicerca.getText().toLowerCase();
        String filtroCucina = comboFiltroCucina.getValue() != null && !comboFiltroCucina.getValue().toString().equals("TUTTO") ? comboFiltroCucina.getValue().toString() : "";
        String filtroPrezzo = comboFiltroPrezzo.getValue() != null && !comboFiltroPrezzo.getValue().toString().equals("TUTTO") ? comboFiltroPrezzo.getValue().toString() : "";
        String filtroRecensioni = comboFiltroRecensioni.getValue() != null && !comboFiltroRecensioni.getValue().toString().equals("TUTTO")? comboFiltroRecensioni.getValue().toString() : "";
        String filtroDelivery = comboFiltroDelivery.getValue() != null && !comboFiltroDelivery.getValue().toString().equals("TUTTO")? comboFiltroDelivery.getValue().toString() : "";
        String filtroPrenotazione = comboFiltroPrenotazione.getValue() != null && !comboFiltroPrenotazione.getValue().toString().equals("TUTTO")? comboFiltroPrenotazione.getValue().toString() : "";
        
        Double filtroDistanza = comboFiltroDistanza.getValue() != null && !comboFiltroDistanza.getValue().toString().equals("50+ km")? comboFiltroDistanza.getValue().getKM() : Double.MAX_VALUE;

        return ristoranti.stream().filter(ristorante-> (ricerca.isEmpty() || ristorante.getNomeRistorante().toLowerCase().contains(ricerca)) && // filtro nome
                                                              (filtroPrezzo.isEmpty() || ristorante.getPrezzo().equals(filtroPrezzo)) && // filtro prezzo
                                                              (filtroCucina.isEmpty() || ristorante.getTipoCucina().name().equals(filtroCucina)) && // filtro cucina
                                                              (filtroRecensioni.isEmpty() || ristorante.getMediaRec() >= filtroRecensioni.length()) &&
                                                              (filtroDelivery.isEmpty() || (filtroDelivery.equals("DELIVERY_DISPONIBILE") && // filtro delivery disponibile
                                                              ristorante.isDelivery()) || (filtroDelivery.equals("DELIVERY_NON_DISPONIBILE") && !ristorante.isDelivery())) &&  // filtro delivery non disponibile
                                                              (filtroPrenotazione.isEmpty() || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_DISPONIBILE") && //filtro prenotazione disponibile
                                                              ristorante.isPrenotazioneOnline()) || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_NON_DISPONIBILE") && !ristorante.isPrenotazioneOnline())) && // filtro prenotazione non disponibile
                                                              (mappaDistanze.get(ristorante.getNomeRistorante()) <= filtroDistanza)).toList(); // filtro distanza
    }


    @FXML
    private void gestisciBottoneUtente(){
        String testo = bottoneRegistratiProfilo.getText().toLowerCase();
        if (testo.equals("registrati")) {
            apriRegistrati();
        } else if (testo.equals("profilo")) {
            if(utenteLoggato.getRuolo().equals(Ruolo.CLIENTE))
                apriProfilo();
            else
                apriProfiloRistoratore();
        }
    }


    private void apriRegistrati(){
        SceneManager.cambioScena(stage, "/GUI/registrazione.fxml", "The Knife - Registrazione", 
            (RegistrazioneController controller) -> {
                controller.setStage(stage);
                controller.setPrincipale(true);
            });
    }


    private void apriProfilo(){
        SceneManager.cambioScena(stage, "/GUI/profilo_cliente.fxml", "The Knife - Profilo", 
            (ProfiloClienteController controller) -> controller.setStage(stage));
    }


    private void apriProfiloRistoratore(){
        SceneManager.cambioScena(stage, "/GUI/profilo_ristoratore.fxml", "The Knife - Registrazione", 
        (ProfiloRistoratoreController controller) -> controller.setStage(stage));
    } 
}
