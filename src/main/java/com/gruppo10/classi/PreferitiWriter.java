package com.gruppo10.classi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class PreferitiWriter {

    private static final String nomeFile = "fileCSV/preferiti.csv";

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

    public static void rimuoviPreferito(int idUtente, int idRistorante) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            List<String[]> listaTemp = new ArrayList<>();
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[0]);
                int idRis = Integer.parseInt(dati[1]);

                if (idUt == idUtente && idRis == idRistorante) {
                    continue; // Salta la riga da rimuovere
                }
                listaTemp.add(dati);
            }

            File file = new File(nomeFile);
            if (file.exists()) {
                file.delete();
            }

            try (CSVWriter writer = new CSVWriter(new FileWriter(nomeFile))) {
                writer.writeNext(new String[]{"ID Utente", "ID Ristorante"});
                for (String[] riga : listaTemp) {
                    writer.writeNext(riga);
                }
            }

        } catch (Exception e) {
            System.err.println("Errore caricamento file csv: " + e.getMessage());
        }
        
        
    }
}
