package com.gruppo10.classi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.CSVWriter;
// ...existing imports...

public class UtenteWriter {

    public void scriviUtente(Utente utente) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileUtente = new File(dir, "utenti.csv");

        boolean fileEsiste = fileUtente.exists();

        try (Writer writer = new FileWriter(fileUtente, true)) {
            // Se il file non esiste, scrivi l'header
            if (!fileEsiste) {
                CSVWriter csvWriter = new CSVWriter(writer);
                String[] header = { "Nome", "Cognome", "Username", "Password", "Data di nascita", "Indirizzo", "Ruolo" }; // Sostituisci con i nomi dei campi della classe Utente
                csvWriter.writeNext(header);
                csvWriter.flush();
                csvWriter.close();
            }

            // Scrivi i dati dell'utente
            StatefulBeanToCsv<Utente> statefulBeanToCsv = new StatefulBeanToCsvBuilder<Utente>(writer).build();
            statefulBeanToCsv.write(utente);
            writer.close();
        }
    }
}


