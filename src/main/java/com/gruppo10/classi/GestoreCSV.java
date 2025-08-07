package com.gruppo10.classi;

import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public abstract class GestoreCSV<T> {
    protected File dir = new File("fileCSV");
    protected File file;

    public GestoreCSV(String f) {
        this.file = new File(dir, f);
    }

    public List<T> caricaCSV() {
        List<T> lista = new ArrayList<>();
        caricamentoExtra();
        try (CSVReader reader = new CSVReader(new FileReader(file))){
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                T obj = parseRiga(dati);
                lista.add(obj);
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv", e.getMessage(), true, nuovoFile -> file = nuovoFile);
            return null;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true,
                        nuovoFile -> file = nuovoFile);
                return null;
            }
        }
        return lista;
    }

    public void caricamentoExtra(){}

    public void scrivi(T obj) {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                GestioneEccezioni.errore("Impossibile creare la directory fileCSV", null, false, null);
            }
        }

        boolean fileEsiste = file.exists();

        try (Writer writer = new FileWriter(file, true); CSVWriter csvWriter = new CSVWriter(writer)) {

            // Scrivi header se il file non esiste
            if (!fileEsiste) {
                csvWriter.writeNext(getHeader());
                csvWriter.flush();
            }

            // Estrai e scrivi i dati
            String[] dati = estraiDati(obj);
            csvWriter.writeNext(dati);
            csvWriter.flush();

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true,
                        nuovoFile -> file = nuovoFile);
            }
        } catch (SecurityException e) {
            GestioneEccezioni.errore("Permesso negato per la scrittura del file", e.getMessage(), true,
                    nuovoFile -> file = nuovoFile);

        } catch (IllegalArgumentException e) {
            GestioneEccezioni.errore("Errore nei dati della recensione", e.getMessage(), false, null);
        }
    }

    public int ultimoID() {
        int contaID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                contaID++;
            }
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true,
                        nuovoFile -> file = nuovoFile);
        }
        return contaID;
    }

    public void sovrascrivi(List<String[]> nuovaLista){
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.file))) {
            writer.writeNext(getHeader());
            for (String[] riga : nuovaLista) {
                writer.writeNext(riga);
            }
        } catch (IOException e) {
           GestioneEccezioni.errore("Errore di scrittura nel file CSV", e.getMessage(), true, nuovoFile -> file = nuovoFile);
        }
    }

    protected abstract String[] getHeader();

    protected abstract String[] estraiDati(T obj);

    protected abstract T parseRiga(String[] dati);
}
