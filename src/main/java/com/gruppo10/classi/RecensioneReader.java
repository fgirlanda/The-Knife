package com.gruppo10.classi;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class RecensioneReader {

    public static List<Recensione> caricaCSV(String nomeFile) {
        List<Recensione> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUtente = Integer.parseInt(dati[1]);
                int idRis = Integer.parseInt(dati[2]);
                int stelle = Integer.parseInt(dati[3]); 
                String testo = dati[4]; 
                String risposta = dati[5]; 
                Recensione r = new Recensione();
                r.setIdUtente(idUtente);
                r.setIdRis(idRis);
                r.setStelle(stelle);
                r.setTesto(testo);
                r.setRisposta(risposta);
                lista.add(r);
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento file csv: " + e.getMessage());
        }
        return lista;
    }
}