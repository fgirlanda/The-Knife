/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
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
import com.opencsv.exceptions.CsvValidationException;

public class GestionePreferiti {

    static File dir = new File("fileCSV");
    static File filePreferiti = new File(dir, "preferiti.csv");

    public static void aggiungiPreferito(int idUtente, int idRistorante) {
        if (!dir.exists())
            dir.mkdirs();

        boolean fileEsiste = filePreferiti.exists();

        // Crea lista dati
        String[] dati = { String.valueOf(idUtente), String.valueOf(idRistorante) };

        try (Writer writer = new FileWriter(filePreferiti, true); CSVWriter csvWriter = new CSVWriter(writer)) {

            // Se il file non esiste, scrivi l'header
            if (!fileEsiste) {
                String[] header = { "ID Utente", "ID Ristorante" };
                csvWriter.writeNext(header);
                csvWriter.flush();
            }

            // Scrivi i dati della recensione
            csvWriter.writeNext(dati);
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + filePreferiti, e, true,
                        file -> filePreferiti = file);
            }
        }
    }

    public static void rimuoviPreferito(int idUtente, int idRistorante) {
        try (CSVReader reader = new CSVReader(new FileReader(filePreferiti))) {
            String[] dati;
            List<String[]> listaTemp = new ArrayList<>();
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[0]);
                int idRis = Integer.parseInt(dati[1]);

                if (idUt == idUtente && idRis == idRistorante) {
                    continue; // Salta la riga da rimuovere
                }
                listaTemp.add(dati);
            }

            if (filePreferiti.exists()) {
                filePreferiti.delete();
            }

            try (CSVWriter writer = new CSVWriter(new FileWriter(filePreferiti))) {
                writer.writeNext(new String[] { "ID Utente", "ID Ristorante" });
                for (String[] riga : listaTemp) {
                    writer.writeNext(riga);
                }
            }

        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv in: " + filePreferiti, e, true,
                    file -> filePreferiti = file);

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + filePreferiti, e, true,
                        file -> filePreferiti = file);
            }
        }
    }

    public static boolean controlloPreferito(int idUtente, int idRistorante) {
        try (CSVReader reader = new CSVReader(new FileReader(filePreferiti))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[0]);
                int idRis = Integer.parseInt(dati[1]);

                if (idUt == idUtente && idRis == idRistorante) {
                    return true;
                }
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv in: " + filePreferiti, e, true, file -> filePreferiti = file);
            return false;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + filePreferiti, e, true,
                        file -> filePreferiti = file);
                return false;
            }
        }

        return false;
    }
}
