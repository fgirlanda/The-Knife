package com.gruppo10.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.FiltroPrezzo;
import com.gruppo10.classi.FiltroTipoCucina;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.FiltroPrezzo;
import com.gruppo10.classi.FiltroTipoCucina;
import com.gruppo10.classi.FiltroDelivey;
import com.gruppo10.classi.FiltroPrenotazione;
import com.gruppo10.classi.FiltroMediaRecensioni;
import com.opencsv.CSVReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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

    @FXML private ComboBox<FiltroDelivey> comboFiltroDelivery;

    @FXML private ComboBox<FiltroPrenotazione> comboFiltroPrenotazione;




    public static List<Ristorante> ristoranti; 

    private HashMap<String, Double> mappaDistanze = new HashMap<>();

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        comboFiltroCucina.getItems().setAll(FiltroTipoCucina.values());
        comboFiltroPrezzo.getItems().setAll(FiltroPrezzo.values());
        comboFiltroRecensioni.getItems().setAll(FiltroMediaRecensioni.values());
        // comboFiltroDelivery.getItems().setAll(FiltroDelivey.values());
        // comboFiltroPrenotazione.getItems().setAll(FiltroPrenotazione.values());
        //caricamento schede ristorante
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti.csv");
        ristoranti = caricaCSV(path.toString());
        caricaTessere(ristoranti);
    }


    @FXML
    public void caricaTessere(List<Ristorante> listaRistoranti) {
        //caricamento schede ristorante
        for (Ristorante r : listaRistoranti) {
            Double dist = utenteLoggato.getCords().calcolaDistanza(r.getCords());
            mappaDistanze.put(r.getNomeRistorante(), dist);
            if (dist <= 10){
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
    public void ricercaRisorante() {
        String ricerca = txtRicerca.getText().toLowerCase();
        String filtroCucina = comboFiltroCucina.getValue() != null && !comboFiltroCucina.getValue().toString().equals("TUTTO") ? comboFiltroCucina.getValue().toString() : "";
        String filtroPrezzo = comboFiltroPrezzo.getValue() != null && !comboFiltroPrezzo.getValue().toString().equals("TUTTO") ? comboFiltroPrezzo.getValue().toString() : "";
        String filtroRecensioni = comboFiltroRecensioni.getValue() != null && !comboFiltroRecensioni.getValue().toString().equals("TUTTO")? comboFiltroRecensioni.getValue().toString() : "";
        String filtroDelivery = comboFiltroDelivery.getValue() != null && !comboFiltroDelivery.getValue().toString().equals("TUTTO")? comboFiltroDelivery.getValue().toString() : "";
        String filtroPrenotazione = comboFiltroPrenotazione.getValue() != null && !comboFiltroPrenotazione.getValue().toString().equals("TUTTO")? comboFiltroPrenotazione.getValue().toString() : "";
        
        contenitoreTessere.getChildren().clear(); // Pulisce il contenitore prima di aggiungere i risultati

        caricaTessere(ristoranti.stream().filter(ristorante-> ristorante.getNomeRistorante().toLowerCase().contains(ricerca) && // filtro nome
                                                              (filtroPrezzo.isEmpty() || ristorante.getPrezzo().equals(filtroPrezzo)) && // filtro prezzo
                                                              (filtroCucina.isEmpty() || ristorante.getTipoCucina().name().equals(filtroCucina)) &&
                                                              (filtroDelivery.isEmpty() || (filtroDelivery.equals("DELIVERY_DISPONIBILE") && ristorante.isDelivery()) || (filtroDelivery.equals("DELIVERY_NON_DISPONIBILE") && !ristorante.isDelivery())) &&  
                                                              (filtroPrenotazione.isEmpty() || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_DISPONIBILE") && ristorante.isPrenotazioneOnline()) || (filtroPrenotazione.equals("PRENOTAZIONE_ONLINE_NON_DISPONIBILE") && !ristorante.isPrenotazioneOnline()))).toList()); // filtro cucina


    }


    
    // Carica i dati da un file CSV e restituisce una lista di oggetti Ristorante
    @FXML
    private List<Ristorante> caricaCSV(String nomeFile) {
        List<Ristorante> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            reader.readNext(); // salta intestazione
            while ((dati = reader.readNext()) != null) {
                String nome = dati[0]; //nome ristorante
                String indirizzo = dati[1]; //indirizzo
                Boolean delivery = Boolean.parseBoolean(dati[2]); //delivery
                Boolean prenotazione = Boolean.parseBoolean(dati[3]); //prenotazione
                String cucina = dati[4]; //cucina
                String prezzo = dati[5]; //prezzo
                String descrizione = dati[6]; //descrizione
                double lat = Double.parseDouble(dati[7]); //latitudine
                double lon = Double.parseDouble(dati[8]); //longitudine
                Ristorante r = new Ristorante();
                r.setNomeRistorante(nome);
                r.setIndirizzo(indirizzo);
                r.setDelivery(delivery);
                r.setPrenotazioneOnline(prenotazione);
                r.setCucina(cucina);
                r.setPrezzo(prezzo);
                r.setDescrizione(descrizione);
                r.setCords(new Coordinate(lat, lon));
                lista.add(r);
            }
        } catch (Exception e) {
            System.out.println("Il file csv non esiste ancora.");
        }
        return lista;
    }

    @FXML
    private void gestisciBottoneUtente(){
        String testo = bottoneRegistratiProfilo.getText().toLowerCase();
        System.out.println(testo);
        if (testo.equals("registrati")) {
            apriRegistrati();
        } else if (testo.equals("profilo")) {
            apriProfilo();
        }
    }

    @FXML
    private void apriRegistrati(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/registrazione.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Registrazione");
            RegistrazioneController controller = loader.getController();
            controller.setStage(stage);

            // Puoi aggiungere animazioni qui se vuoi (es. fade)

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

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Profilo");
            ProfiloClienteController controller = loader.getController();
            controller.setStage(stage);

            // Puoi aggiungere animazioni qui se vuoi (es. fade)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}
