package com.gruppo10.classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class RecensioneWriter {
    private static final String nomeFile = "fileCSV/recensioni.csv";

    public void scriviRecensione(Recensione recensione) throws IOException{
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileRecensioni = new File(dir, "recensioni.csv");

        boolean fileEsiste = fileRecensioni.exists();

        
        try (Writer writer = new FileWriter(fileRecensioni, true)) {

            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileEsiste) {
                String[] header = { "ID Recensione", "Nome cliente", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"};
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
        String[] dati = new String[7];
        
        dati[0] = Integer.toString(RistoranteWriter.ultimoID(file));
        dati[1] = recensione.getNomeUtente();
        dati[2] = Integer.toString(recensione.getIdUtente());
        dati[3] = Integer.toString(recensione.getIdRis());
        dati[4] = Integer.toString(recensione.getStelle());
        dati[5] = recensione.getTesto();
        dati[6] = recensione.getRisposta();

        return dati;
    }    
    
    public static void aggiungiRisposta(Recensione recensione, String risposta){
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            List<String[]> listaTemp = new ArrayList<>();
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[2]);
                int idRis = Integer.parseInt(dati[3]);

                if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                    dati[6] = risposta;
                }
                listaTemp.add(dati);
            }

            File file = new File(nomeFile);
            if (file.exists()) {
                file.delete();
            }

            try (CSVWriter writer = new CSVWriter(new FileWriter(nomeFile))) {
                writer.writeNext(new String[]{"ID Recensione", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"});
                for (String[] riga : listaTemp) {
                    writer.writeNext(riga);
                }
            }

        } catch (Exception e) {
            System.err.println("Errore caricamento file csv: " + e.getMessage());
        }  
    }

    public static void modificaRecensione(Recensione recensione, String testoModificato){
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            List<String[]> listaTemp = new ArrayList<>();
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[2]);
                int idRis = Integer.parseInt(dati[3]);

                if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                    dati[5] = testoModificato;
                }
                listaTemp.add(dati);
            }

            File file = new File(nomeFile);
            if (file.exists()) {
                file.delete();
            }

            try (CSVWriter writer = new CSVWriter(new FileWriter(nomeFile))) {
                writer.writeNext(new String[]{"ID Recensione", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"});
                for (String[] riga : listaTemp) {
                    writer.writeNext(riga);
                }
            }

        } catch (Exception e) {
            System.err.println("Errore caricamento file csv: " + e.getMessage());
        }  
    }
}
