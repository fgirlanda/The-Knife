package com.gruppo10.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Ristorante;
import com.opencsv.CSVReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaginaPrincipaleController {

    private Stage stage;
    @FXML
    private Button bottoneRegistratiProfilo;

    @FXML private VBox contenitoreTessere;

    @FXML private TextField txtRicerca;

    @FXML private Button btnCerca;


    static List<Ristorante> ristoranti; 

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        //caricamento schede ristorante
        Path path = Paths.get(System.getProperty("user.dir"), "fileCSV", "ristoranti_nuovi.csv");
        ristoranti = caricaCSV(path.toString());
        caricaTessere(ristoranti);
    }


    @FXML
    public void caricaTessere(List<Ristorante> listaRistoranti) {
        //caricamento schede ristorante
        for (Ristorante r : listaRistoranti) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/card_ristorante.fxml"));
                HBox card = loader.load();

                CardRistoranteController controller = loader.getController();
                controller.setDati(r);

                contenitoreTessere.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }

    @FXML
    public void ricercaRisorante() {
        String ricerca = txtRicerca.getText().toLowerCase();
        contenitoreTessere.getChildren().clear(); // Pulisce il contenitore prima di aggiungere i risultati
        caricaTessere(ristoranti.stream().filter(ristorante-> ristorante.getNomeRistorante().toLowerCase().contains(ricerca)).toList());


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
            String prezzo = dati[5]; //prezzo
            Ristorante r = new Ristorante();
            r.setNomeRistorante(nome);
            r.setPrezzo(prezzo);
            lista.add(r);
        }
        } catch (Exception e) {
            e.printStackTrace();
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

    @FXML
    private void apri_aggiungi_ristorante() {
    try {
        // Carica il file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/aggiungi_ristorante.fxml"));
        Parent root = loader.load();

        // Crea un nuovo stage per il dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Aggiungi Ristorante");
        dialogStage.initModality(Modality.APPLICATION_MODAL); // blocca l'interazione con altre finestre
        dialogStage.initOwner(stage); // stage principale, da passare se disponibile
        dialogStage.setScene(new Scene(root));

        // Se il controller ha bisogno dello stage, puoi passarlo:
        AggiungiRistoranteController controller = loader.getController();
        controller.setStage(dialogStage); // opzionale, se hai un metodo per settarlo

        dialogStage.showAndWait(); // mostra e attende la chiusura

    } catch (IOException e) {
        e.printStackTrace();
    }
    
    
}
}
