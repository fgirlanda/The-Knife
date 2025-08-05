package com.gruppo10.classi;

import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class PreferitiReader {

    private static final String nomeFile = "fileCSV/preferiti.csv";

    public static boolean controlloPreferito(int idUtente, int idRistorante) {
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[0]);
                int idRis = Integer.parseInt(dati[1]);

                if (idUt == idUtente && idRis == idRistorante) {
                    return true;
                }
            }
        } catch (CsvValidationException e) {
            System.err.println("Errore format csv." + e.getMessage());
        } catch (IOException e) {
            System.err.println("Errore caricamento file: " + nomeFile);
        } 
        
        return false;
    }
}
