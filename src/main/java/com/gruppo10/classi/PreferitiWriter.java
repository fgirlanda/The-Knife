package com.gruppo10.classi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.opencsv.CSVWriter;

public class PreferitiWriter {
    
    public static void aggiungiPreferito(int idUtente, int idRistorante) throws IOException{
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File filePreferiti = new File(dir, "preferiti.csv");

        boolean fileEsiste = filePreferiti.exists();

        // Crea lista dati
        String[] dati = { String.valueOf(idUtente), String.valueOf(idRistorante) };

        try (Writer writer = new FileWriter(filePreferiti, true)) {

            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileEsiste) {
                String[] header = { "ID Utente", "ID Ristorante"};
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            
            // Scrivi i dati della recensione
            csvWriter.writeNext(dati);
            csvWriter.close();
            writer.close();
        }
    }
}
