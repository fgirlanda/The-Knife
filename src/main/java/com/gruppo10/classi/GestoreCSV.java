/* 
Francesco Girlanda 760616 VA
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

/**
 * Classe astratta per la gestione di file CSV contenenti oggetti
 * identificabili.
 * <p>
 * Fornisce metodi per caricare, scrivere, sovrascrivere e rimuovere elementi
 * dal CSV.
 * </p>
 * 
 * @param <T> Tipo di oggetto gestito, deve implementare {@link Identificabile}
 */
public abstract class GestoreCSV<T extends Identificabile> {

    /** Directory principale dei file CSV. */
    protected File dir = new File("../data/fileCSV");

    /** File CSV specifico gestito dall'istanza. */
    protected File file;

    /**
     * Costruttore della classe.
     *
     * @param f nome del file CSV da gestire
     */
    public GestoreCSV(String f) {
        this.file = new File(dir, f);
    }

    /**
     * Restituisce l'header del CSV come array di stringhe.
     *
     * @return header del CSV
     */
    protected abstract String[] getHeader();

    /**
     * Estrae i dati di un oggetto T come array di stringhe da scrivere nel CSV.
     *
     * @param obj oggetto da estrarre
     * @return array di stringhe rappresentante l'oggetto
     */
    protected abstract String[] estraiDati(T obj);

    /**
     * Converte una riga del CSV in un oggetto T.
     *
     * @param dati array di stringhe rappresentante la riga
     * @return oggetto T corrispondente
     */
    protected abstract T parseRiga(String[] dati);

    /**
     * Carica tutti gli oggetti dal CSV in una lista.
     * <p>
     * Eventuali errori di lettura o validazione vengono gestiti tramite
     * {@link GestioneEccezioni}.
     * </p>
     *
     * @return lista di oggetti T presenti nel CSV
     */
    public List<T> caricaCSV() {
        List<T> lista = new ArrayList<>();
        caricamentoExtra();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext();
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

    /**
     * Metodo opzionale per caricare dati aggiuntivi prima del caricamento CSV.
     * <p>
     * È sovrascritto dalla classe figlia {@link RistoranteCSV} per caricare le recensioni.
     * </p>
     */
    public void caricamentoExtra() {
    }

    /**
     * Scrive un oggetto T nel CSV.
     * <p>
     * Se il file non esiste, viene creato e viene scritto l'header.
     * </p>
     *
     * @param obj oggetto da scrivere
     */
    public void scrivi(T obj) {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                GestioneEccezioni.errore("Impossibile creare la directory fileCSV", null, false, null);
            }
        }

        boolean fileEsiste = file.exists();

        try (Writer writer = new FileWriter(file, true); CSVWriter csvWriter = new CSVWriter(writer)) {

            if (!fileEsiste) {
                csvWriter.writeNext(getHeader());
                csvWriter.flush();
            }

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

    /**
     * Sovrascrive completamente il CSV con una nuova lista di righe. Serve per modificare una riga nel CSV.
     *
     * @param nuovaLista lista di righe da scrivere nel CSV
     */
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

    /**
     * Rimuove un oggetto T dal CSV.
     * <p>
     * La rimozione è effettuata confrontando gli ID utente e ristorante
     * dell'oggetto, sia per i preferiti che per le recensioni.
     * </p>
     * <p>Nota: I ristoranti non possono essere rimossi/modificati</p>
     *
     * @param obj oggetto da rimuovere
     */
    public void rimuovi(T obj) {
        List<String[]> listaTemp = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext();

            while ((dati = reader.readNext()) != null) {
                try {
                    int idUt = Integer.parseInt(dati[0]);
                    int idRis = Integer.parseInt(dati[1]);

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

    /**
     * Restituisce l'ultimo ID utilizzato nel CSV.
     * <p>
     * Viene calcolato contando il numero di righe del file.
     * </p>
     *
     * @return numero di righe nel CSV (ultimo ID)
     */
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