package com.gruppo10.classi;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Data;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

@Data

public class Utente {
    @CsvBindByName
    private String nome;
    @CsvBindByName
    private String cognome;
    @CsvBindByName
    private String username;
    @CsvBindByName
    private String password;
    @CsvCustomBindByName(converter = LocalDateConverter.class)
    private LocalDate dataDiNascita;
    @CsvBindByName
    private String indirizzo;
    @CsvCustomBindByName(converter = RuoloConverter.class)
    private Ruolo ruolo;

    private Coordinate cords;
    private ArrayList<Ristorante> ristorantiPreferiti;
    private ArrayList<Ristorante> ristorantiOwned;


    public void setDataDiNascita(String dataDiNascita) {
        String[] parts = dataDiNascita.split("-");
        int giorno = Integer.parseInt(parts[0]);
        int mese = Integer.parseInt(parts[1]);
        int anno = Integer.parseInt(parts[2]);
        this.dataDiNascita = LocalDate.of(anno, mese, giorno);
    }

    public void setRuolo(String ruolo) {
        try {
            this.ruolo = Ruolo.valueOf(ruolo.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.ruolo = Ruolo.NON_REGISTRATO; // Valore di default se non riconosciuto
        }
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", dataDiNascita=" + dataDiNascita +
                ", indirizzo='" + indirizzo + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }
}
