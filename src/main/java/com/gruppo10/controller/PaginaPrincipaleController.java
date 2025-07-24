package com.gruppo10.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import com.gruppo10.classi.RistoranteReader;
import com.gruppo10.classi.FiltroPrezzo;
import com.gruppo10.classi.FiltroTipoCucina;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.FiltroDelivery;
import com.gruppo10.classi.FiltroDistanza;
import com.gruppo10.classi.FiltroPrenotazione;
import com.gruppo10.classi.FiltroMediaRecensioni;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaginaPrincipaleController {

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private Stage stage;
    @FXML
    private Button bottoneRegistratiProfilo;

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

    private HashMap<String, Double> mappaDistanze = new HashMap<>();

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
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
        for(FiltroDistanza v: FiltroDistanza.values()){
            System.out.println(v);
        }
        comboFiltroPrenotazione.getItems().setAll(FiltroPrenotazione.values());

        // Caricamento schede ristorante
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti.csv");
        ristoranti = RistoranteReader.caricaCSV(path.toString());
        caricaTessere(ristoranti);
    }


    // Caricamento schede ristorante
    @FXML
    public void caricaTessere(List<Ristorante> listaRistoranti) {
        for (Ristorante r : listaRistoranti) {
            Double dist = utenteLoggato.getCords().calcolaDistanza(r.getCords());
            mappaDistanze.put(r.getNomeRistorante(), dist);
            if (dist <= 20000){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/card_ristorante.fxml"));
                    HBox card = loader.load();

                    CardRistoranteController controller = loader.getController();
                    controller.setDati(r);

                    contenitoreTessere.getChildren().add(card);
                } catch (IOException e) {
                    System.err.println("Errore nel caricamento della scheda del ristorante: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    public void ricercaRistorante() {
        String ricerca = txtRicerca.getText().toLowerCase();
        String filtroCucina = comboFiltroCucina.getValue() != null && !comboFiltroCucina.getValue().toString().equals("TUTTO") ? comboFiltroCucina.getValue().toString() : "";
        String filtroPrezzo = comboFiltroPrezzo.getValue() != null && !comboFiltroPrezzo.getValue().toString().equals("TUTTO") ? comboFiltroPrezzo.getValue().toString() : "";
        String filtroRecensioni = comboFiltroRecensioni.getValue() != null && !comboFiltroRecensioni.getValue().toString().equals("TUTTO")? comboFiltroRecensioni.getValue().toString() : "";
        String filtroDelivery = comboFiltroDelivery.getValue() != null && !comboFiltroDelivery.getValue().toString().equals("TUTTO")? comboFiltroDelivery.getValue().toString() : "";
        String filtroPrenotazione = comboFiltroPrenotazione.getValue() != null && !comboFiltroPrenotazione.getValue().toString().equals("TUTTO")? comboFiltroPrenotazione.getValue().toString() : "";
        
        Double filtroDistanza = comboFiltroDistanza.getValue() != null && !comboFiltroDistanza.getValue().toString().equals("50+ km")? comboFiltroDistanza.getValue().getKM() : Double.MAX_VALUE;

        contenitoreTessere.getChildren().clear(); // Pulisce il contenitore prima di aggiungere i risultati

        caricaTessere(ristoranti.stream().filter(ristorante-> (!ricerca.isEmpty() || ristorante.getNomeRistorante().toLowerCase().contains(ricerca)) && // filtro nome
                                                              (filtroPrezzo.isEmpty() || ristorante.getPrezzo().equals(filtroPrezzo)) && // filtro prezzo
                                                              (filtroCucina.isEmpty() || ristorante.getTipoCucina().name().equals(filtroCucina)) && // filtro cucina
                                                              (filtroDelivery.isEmpty() || (filtroDelivery.equals("DELIVERY_DISPONIBILE") && // filtro delivery disponibile
                                                              ristorante.isDelivery()) || (filtroDelivery.equals("DELIVERY_NON_DISPONIBILE") && !ristorante.isDelivery())) &&  // filtro delivery non disponibile
                                                              (filtroPrenotazione.isEmpty() || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_DISPONIBILE") && //filtro prenotazione disponibile
                                                              ristorante.isPrenotazioneOnline()) || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_NON_DISPONIBILE") && !ristorante.isPrenotazioneOnline())) && // filtro prenotazione non disponibile
                                                              (mappaDistanze.get(ristorante.getNomeRistorante()) <= filtroDistanza)).toList()); // filtro distanza

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

    @FXML
    private void apriRegistrati(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/registrazione.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra
            stage.setScene(scene);
            stage.setTitle("The Knife - Registrazione");
            RegistrazioneController controller = loader.getController();
            controller.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void apriProfilo(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/profilo_cliente.fxml")); //gestire ruolo cliente o ristoratore
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra
            stage.setScene(scene);
            stage.setTitle("The Knife - Profilo");
            ProfiloClienteController controller = loader.getController();
            controller.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    @FXML
    private void apriProfiloRistoratore(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/profilo_ristoratore.fxml")); //gestire ruolo cliente o ristoratore
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Profilo");
            ProfiloRistoratoreController controller = loader.getController();
            controller.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
}
