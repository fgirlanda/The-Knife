package com.gruppo10.classi;

import lombok.Data;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

@Data
public class Ristorante {
    
    private Coordinate cords;

    @CsvBindByName
    private String nomeRistorante;
    @CsvBindByName
    private String indirizzo;
    @CsvBindByName
    private boolean delivery;
    @CsvBindByName
    private boolean prenotazioneOnline;
    @CsvCustomBindByName(converter = TipoCucinaConverter.class)
    private TipoCucina tipoCucina;
    @CsvBindByName
    private String prezzo;
    @CsvBindByName
    private String descrizione;

    private ArrayList<Recensione> recensioni;

    public void setCucina(String value) {
        try {
            this.tipoCucina = TipoCucina.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            this.tipoCucina = TipoCucina.INTERNAZIONALE; // Valore di default se non riconosciuto
        }
    }  
}
