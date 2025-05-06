package com.gruppo10.classi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import com.opencsv.CSVWriter;

public class utenteWriter {
    
    public void scriviUtente(Utente utente) throws IOException {
        Writer writer = new FileWriter("/fileCSV/utenti.csv", true);
        StatefulBeanToCsv<Utente> statefulBeanToCsv = new StatefulBeanToCsvBuilder<Utente>(writer).build();
        // try (CSVWriter csvWriter = new CSVWriter(writer)) {
        //     String[] record = {utente.getNome(), utente.getCognome(), utente.getUsername(), utente.getPassword(), utente.getDataDiNascita().toString(), utente.getIndirizzo(), utente.getRuolo().name()};
        //     csvWriter.writeNext(record);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
