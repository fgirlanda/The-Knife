package com.gruppo10.classi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.CSVWriter;

public class UtenteWriter {

    public void scriviUtente(Utente utente) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileUtente = new File(dir, "utenti.csv");

        //crea lista dati
        String[] dati = estraiDati(utente);
        List<String[]> listaDati = new ArrayList<>();
        listaDati.add(dati);

        boolean fileEsiste = fileUtente.exists();

        try (Writer writer = new FileWriter(fileUtente, true)) {
            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileEsiste) {
                String[] header = { "Nome", "Cognome", "Username", "Password", "Data di nascita", "Indirizzo", "Ruolo" }; // Sostituisci con i nomi dei campi della classe Utente
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            
            // Scrivi i dati dell'utente
            csvWriter.writeAll(listaDati);
            csvWriter.close();
            writer.close();
        }
    }

    private String[] estraiDati(Utente utente) {
        String[] dati = new String[7];
        dati[0] = utente.getNome();
        dati[1] = utente.getCognome();
        dati[2] = utente.getUsername();
        dati[3] = utente.getPassword();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dati[4] = utente.getDataDiNascita().format(formatter).toString();
        dati[5] = utente.getIndirizzo();
        dati[6] = utente.getRuolo().toString();
        return dati;
    }
}


