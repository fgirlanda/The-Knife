package com.gruppo10.classi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.gruppo10.controller.LoginController;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

// Aggiungere id, shiftare tutti i dati
public class RistoranteWriter {

    private Utente utenteLoggato = LoginController.utenteLoggato;

    public void scriviRistorante(Ristorante ristorante) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileRistorante = new File(dir, "ristoranti_nuovi.csv");

        // Crea lista dati
        String[] dati = estraiDati(ristorante);

        boolean nuovoFile = !fileRistorante.exists();
        try (Writer writer = new FileWriter(fileRistorante, true)) {
            CSVWriter csvWriter = new CSVWriter(writer);
            if (nuovoFile) {
                String[] header = { "Id", "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina", "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario"};
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            
            // Scrivi i dati del ristorante
            csvWriter.writeNext(dati);
            csvWriter.close();
            writer.close();
        }
    }

    private String[] estraiDati(Ristorante ristorante) {
        String[] dati = new String[11];
        dati[0] = String.valueOf(trovaUltimoId());
        dati[1] = ristorante.getNomeRistorante();
        dati[2] = ristorante.getIndirizzo();
        dati[3] = String.valueOf(ristorante.isDelivery());
        dati[4] = String.valueOf(ristorante.isPrenotazioneOnline());
        dati[5] = ristorante.getTipoCucina().toString();
        dati[6] = ristorante.getPrezzo();
        dati[7] = ristorante.getDescrizione();
        Double lat = ristorante.getCords().getLat();
        Double lon = ristorante.getCords().getLon();
        dati[8] = lat.toString();
        dati[9] = lon.toString();
        dati[10] = String.valueOf(utenteLoggato.getId());
        return dati;
    }  

    private static int trovaUltimoId() {
        File fileRistorante = new File("fileCSV/ristoranti.csv");
        if (!fileRistorante.exists()) {
            return 1; // Se il file non esiste, ritorna 0
        }

        try (CSVReader reader = new CSVReader(new FileReader(fileRistorante))) {
            List<String[]> allRows = reader.readAll();
            if (allRows.size() <= 1) {
                return 1; // Solo l'header, ritorna 0
            }
            String[] lastRow = allRows.get(allRows.size() - 1);
            return Integer.parseInt(lastRow[0])+1; // Assumendo che l'ID sia nella prima colonna
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // In caso di errore, ritorna 0
        }
    }
}
