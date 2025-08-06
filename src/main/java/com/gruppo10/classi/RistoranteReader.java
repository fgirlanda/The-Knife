package com.gruppo10.classi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class RistoranteReader {
    public static List<Ristorante> caricaCSV() {
        File dir = new File("fileCSV");
        File fileRistoranti = new File(dir, "ristoranti.csv");
        List<Recensione> listaRecensioni = new ArrayList<>();
        listaRecensioni = RecensioneReader.caricaCSV();
        
        List<Ristorante> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileRistoranti))) {
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
                r.setIdproprietario(idproprietario);
                if(listaRecensioni != null){
                    for (Recensione rec : listaRecensioni) {
                        if (rec.getIdRis() == id) {
                            r.aggiungiRecensione(rec);
                        }
                    }
                }
                lista.add(r);
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv in: " + fileRistoranti);
            return null;

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + fileRistoranti);
            return null;
        }  
        return lista;
    }
}
