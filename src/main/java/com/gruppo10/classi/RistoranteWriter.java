package com.gruppo10.classi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

// Aggiungere id, shiftare tutti i dati
public class RistoranteWriter {

    public void scriviRistorante(Ristorante ristorante) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileRistorante = new File(dir, "ristoranti_nuovi.csv");

        // Crea lista dati
        String[] dati = estraiDati(ristorante);

        try (Writer writer = new FileWriter(fileRistorante, true)) {

            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileRistorante.exists()) {
                String[] header = { "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina", "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario"};
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
        String[] dati = new String[10];
        dati[0] = ristorante.getNomeRistorante();
        dati[1] = ristorante.getIndirizzo();
        dati[2] = String.valueOf(ristorante.isDelivery());
        dati[3] = String.valueOf(ristorante.isPrenotazioneOnline());
        dati[4] = ristorante.getTipoCucina().toString();
        dati[5] = ristorante.getPrezzo();
        dati[6] = ristorante.getDescrizione();
        Double lat = ristorante.getCords().getLat();
        Double lon = ristorante.getCords().getLon();
        dati[7] = lat.toString();
        dati[8] = lon.toString();
        // dati[9] = utenteLoggato.getUsername();
        return dati;
    }  
}
