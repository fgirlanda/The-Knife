/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
*/
package com.gruppo10.classi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class PreferitiCSV extends GestoreCSV<Preferito> {
    
    static String f = "preferiti.csv";
    
    public PreferitiCSV() {
        super(f);
    }

    @Override
    public String[] getHeader() {
        String[] header = { "ID Utente", "ID Ristorante" };
        return header;
    }
    
    @Override
    public String[] estraiDati(Preferito preferito) {
        String[] dati = new String[2];
    
        dati[0] = Integer.toString(preferito.getIdUtente());
        dati[1] = Integer.toString(preferito.getIdRistorante());
    
        return dati;
    }
    
    @Override
    public Preferito parseRiga(String[] dati) {
        int idUtente = Integer.parseInt(dati[0]);
        int idRistorante = Integer.parseInt(dati[1]);
    
        return new Preferito(idUtente, idRistorante);
    }

    public boolean controlloPreferito(int idUtente, int idRistorante) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
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
            GestioneEccezioni.errore("Errore format csv in: " + file, e, true, nuovoFile -> file = nuovoFile);
            return false;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + file, e, true,
                        nuovoFile -> file = nuovoFile);
                return false;
            }
        }

        return false;
    }
}
