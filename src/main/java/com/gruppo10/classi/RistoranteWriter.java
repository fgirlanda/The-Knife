package com.gruppo10.classi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.opencsv.CSVWriter;

public class RistoranteWriter {
    static File dir = new File("fileCSV");

    static File fileRistoranti = new File(dir, "ristoranti_test.csv");

    public static void scriviRistorante(Ristorante ristorante) {
        if (!dir.exists())
            dir.mkdirs();

        boolean fileEsiste = fileRistoranti.exists();

        try (Writer writer = new FileWriter(fileRistoranti, true); CSVWriter csvWriter = new CSVWriter(writer);) {
            if (!fileEsiste) {
                String[] header = { "Id", "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina",
                        "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario" };
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            String[] dati = estraiDati(ristorante, fileRistoranti);
            csvWriter.writeNext(dati);
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, file -> fileRistoranti = file);
        }
    }

    private static String[] estraiDati(Ristorante ristorante, File file) {
        String[] dati = new String[11];
        dati[0] = String.valueOf(ultimoID(file));
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
        dati[10] = String.valueOf(ristorante.getIdproprietario());
        return dati;
    }

    public static int ultimoID(File file) {
        int contaID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                contaID++;
            }
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, nuovoFile -> fileRistoranti = nuovoFile);
        }
        return contaID;
    }
}
