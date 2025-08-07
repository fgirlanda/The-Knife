package com.gruppo10.classi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class RecensioneReader{
    static File dir = new File("fileCSV");
    static File fileRecensioni = new File(dir, "recensioni2.csv");

    public static List<Recensione> caricaCSV() {
        List<Recensione> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileRecensioni))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                String nomeUtente = dati[1];
                int idUtente = Integer.parseInt(dati[2]);
                int idRis = Integer.parseInt(dati[3]);
                int stelle = Integer.parseInt(dati[4]); 
                String testo = dati[5]; 
                String risposta = dati[6]; 
                Recensione r = new Recensione();
                r.setUsername(nomeUtente);
                r.setIdUtente(idUtente);
                r.setIdRis(idRis);
                r.setStelle(stelle);
                r.setTesto(testo);
                r.setRisposta(risposta);
                lista.add(r);
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv", e.getMessage(), true, file -> fileRecensioni = file);
            return null;
            
        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, file -> fileRecensioni = file);
            return null;
        }

        return lista;
    }
}