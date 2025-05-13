package com.gruppo10.classi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class RistoranteWriter {

    public void scriviRistorante(Ristorante ristorante) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileRisotrante = new File(dir, "ristoranti.csv");

        //crea lista dati
        String[] dati = estraiDati(ristorante);
        List<String[]> listaDati = new ArrayList<>();
        listaDati.add(dati);

        boolean fileEsiste = fileRisotrante.exists();

        try (Writer writer = new FileWriter(fileRisotrante, true)) {
            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileEsiste) {
                String[] header = { "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina" }; // Sostituisci con i nomi dei campi della classe Utente
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            
            // Scrivi i dati del ristorante
            csvWriter.writeAll(listaDati);
            csvWriter.close();
            writer.close();
        }
    }

    private String[] estraiDati(Ristorante ristorante) {
        String[] dati = new String[7];
        dati[0] = ristorante.getNomeRistorante();
        dati[1] = ristorante.getIndirizzo();
        dati[2] = String.valueOf(ristorante.isDelivery());
        dati[3] = String.valueOf(ristorante.isPrenotazioneOnline());
        dati[4] = ristorante.getTipoCucina().toString();
        return dati;
    }
    
}
