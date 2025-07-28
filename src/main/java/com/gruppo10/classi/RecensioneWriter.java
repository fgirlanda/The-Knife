package com.gruppo10.classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.opencsv.CSVWriter;

public class RecensioneWriter {
    
    public void scriviRecensione(Recensione recensione) throws IOException{
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileRecensioni = new File(dir, "recensioni.csv");

        boolean fileEsiste = fileRecensioni.exists();

        
        try (Writer writer = new FileWriter(fileRecensioni, true)) {

            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileEsiste) {
                String[] header = { "ID Recensione", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"};
                csvWriter.writeNext(header);
                csvWriter.flush();
            }

            // Crea lista dati
            String[] dati = estraiDati(recensione, fileRecensioni);

            // Scrivi i dati della recensione
            csvWriter.writeNext(dati);
            csvWriter.close();
            writer.close();
        }
    }

    private String[] estraiDati(Recensione recensione, File file) throws FileNotFoundException, IOException {
        String[] dati = new String[6];
        
        dati[0] = Integer.toString(RistoranteWriter.ultimoID(file));
        dati[1] = Integer.toString(recensione.getIdUtente());
        dati[2] = Integer.toString(recensione.getIdRis());
        dati[3] = Integer.toString(recensione.getStelle());
        dati[4] = recensione.getTesto();
        dati[5] = recensione.getRisposta();

        return dati;
    }          
}
