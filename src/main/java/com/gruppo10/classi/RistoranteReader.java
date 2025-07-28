package com.gruppo10.classi;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.controller.CardRistoranteController;
import com.opencsv.CSVReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RistoranteReader {
    public static List<Ristorante> caricaCSV(String nomeFile) {
        List<Ristorante> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int id = Integer.parseInt(dati[0]);
                String nome = dati[1]; 
                String indirizzo = dati[2]; 
                Boolean delivery = Boolean.parseBoolean(dati[3]); 
                Boolean prenotazione = Boolean.parseBoolean(dati[4]); 
                String cucina = dati[5]; 
                String prezzo = dati[6]; 
                String descrizione = dati[7]; 
                double lat = Double.parseDouble(dati[8]); 
                double lon = Double.parseDouble(dati[9]); 
                int idproprietario = Integer.parseInt(dati[10]); // Aggiunto per l'id del proprietario
                Ristorante r = new Ristorante();
                r.setId(id);
                r.setNomeRistorante(nome);
                r.setIndirizzo(indirizzo);
                r.setDelivery(delivery);
                r.setPrenotazioneOnline(prenotazione);
                r.setCucina(cucina);
                r.setPrezzo(prezzo);
                r.setDescrizione(descrizione);
                r.setCords(new Coordinate(lat, lon));
                r.setIdproprietario(idproprietario); // Imposta l'id del proprietario
                lista.add(r);
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento file csv: " + e.getMessage());
        }
        return lista;
    }


    @FXML
    public static void caricaTessere(List<Ristorante> listaRistoranti, VBox contenitoreTessere, Stage stage, boolean paginaPrincipale){
        for (Ristorante r : listaRistoranti) {
            try {
                FXMLLoader loader = new FXMLLoader(RistoranteReader.class.getResource("/GUI/card_ristorante.fxml"));
                HBox card = loader.load();

                CardRistoranteController controller = loader.getController();
                controller.setStage(stage);
                controller.setRistorante(r);
                controller.setDati();
                controller.setPrincipale(paginaPrincipale);
                
                contenitoreTessere.getChildren().add(card);
            } catch (IOException e) {
                System.err.println("Errore nel caricamento della scheda del ristorante: " + e.getMessage());
            }
        }
    }
}
