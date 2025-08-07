package com.gruppo10.classi;

import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class GestoreCSV<T> {
    protected File file;

    public GestoreCSV(File file) {
        this.file = file;
    }

    public List<T> caricaCSV() {
        List<T> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                T obj = parseLine(dati);
                lista.add(obj);
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv", e.getMessage(), true, nuovoFile -> file = nuovoFile);
            return null;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, nuovoFile -> file = nuovoFile);
                return null;
            }
        }
        return lista;
    }

    public int ultimoID() {
        int contaID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                contaID++;
            }
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, nuovoFile -> file = nuovoFile);
        }
        return contaID;
    }

    protected abstract String[] getHeader();

    protected abstract String[] estraiDati(T obj);

    protected abstract T parseLine(String[] dati);
}
