/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
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
import java.util.Arrays;

public abstract class GestoreCSV<T extends InterfacciaIdentificabile> {
    protected File dir = new File("fileCSV");
    protected File file;
    
    public GestoreCSV(String f) {
        this.file = new File(dir, f);
    }
    
    protected abstract String[] getHeader();

    protected abstract String[] estraiDati(T obj);

    protected abstract T parseRiga(String[] dati);

    public List<T> caricaCSV() {
        List<T> lista = new ArrayList<>();
        caricamentoExtra();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                T obj = parseRiga(dati);
                lista.add(obj);
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv in: " + file, e, true, nuovoFile -> file = nuovoFile);
            return null;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + file, e, true,
                        nuovoFile -> file = nuovoFile);
                return null;
            }
        }
        return lista;
    }

    public void caricamentoExtra() {
    }

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
                GestioneEccezioni.errore("Errore caricamento file: " + file, e, true,
                        nuovoFile -> file = nuovoFile);
            }
        } catch (SecurityException e) {
            GestioneEccezioni.errore("Permesso negato per la scrittura del file: " + file, e, true,
                    nuovoFile -> file = nuovoFile);

        } catch (IllegalArgumentException e) {
            GestioneEccezioni.errore("Errore nei dati della recensione", e, false, null);
        }
    }

    public void sovrascrivi(List<String[]> nuovaLista) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.file))) {
            writer.writeNext(getHeader());
            for (String[] riga : nuovaLista) {
                writer.writeNext(riga);
            }
        } catch (IOException e) {
            GestioneEccezioni.errore("Errore di scrittura nel file CSV: " + file, e, true,
                    nuovoFile -> file = nuovoFile);
        }
    }

    public void rimuovi(T obj) {
        List<String[]> listaTemp = new ArrayList<>();

        // Lettura del CSV
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext(); // Salta l'header

            while ((dati = reader.readNext()) != null) {
                try {
                    int idUt = Integer.parseInt(dati[0]);
                    int idRis = Integer.parseInt(dati[1]);

                    // Aggiungi tutte le righe tranne quella da rimuovere
                    if (!(idUt == obj.getIdUtente() && idRis == obj.getIdRistorante())) {
                        listaTemp.add(dati);
                    }
                } catch (NumberFormatException e) {
                    GestioneEccezioni.errore("Errore parsing ID\nRiga: " + Arrays.toString(dati), e, false, null);
                }
            }

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + file, e, true, nuovoFile -> file = nuovoFile);
            return;

        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore di validazione CSV: " + file, e, true, nuovoFile -> file = nuovoFile);
            return;
        }

        sovrascrivi(listaTemp);
    }

    public int ultimoID() {
        int contaID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                contaID++;
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException)
                contaID++;
            else
                GestioneEccezioni.errore("Errore caricamento file: " + file, e, true,
                        nuovoFile -> file = nuovoFile);
        }
        return contaID;
    }

}
