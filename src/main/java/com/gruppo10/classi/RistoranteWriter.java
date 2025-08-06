package com.gruppo10.classi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.opencsv.CSVWriter;

// Aggiungere id, shiftare tutti i dati
public class RistoranteWriter {

    public void scriviRistorante(Ristorante ristorante){
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileRistorante = new File(dir, "ristoranti.csv");

        boolean fileEsiste = fileRistorante.exists();
        
        try (Writer writer = new FileWriter(fileRistorante, true); CSVWriter csvWriter = new CSVWriter(writer);) {
            if (!fileEsiste) {
                String[] header = { "Id", "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina", "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario"};
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            // Crea lista dati
            String[] dati = estraiDati(ristorante, fileRistorante);
            // Scrivi i dati del ristorante
            csvWriter.writeNext(dati);
        } catch (IOException e) {
            System.err.println("Errore caricamento file: " + fileRistorante.toString());
        }
    }


    private String[] estraiDati(Ristorante ristorante, File file){
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


    public static int ultimoID(File file){
        int contaID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while (br.readLine() != null) {
                    contaID++;
                }
        } catch (IOException e) {
            System.err.println("Errore caricamento file: " + e.getMessage());
        } 
        return contaID;
    }
}
