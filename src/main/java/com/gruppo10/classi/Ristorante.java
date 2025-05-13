package com.gruppo10.classi;

import lombok.Data;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

@Data
public class Ristorante {
    // private String nomeRistorante;
    // private String indirizzo;
    // private boolean delivery;
    // private boolean prenotazioneOnline;
    // private String tipoCucina;

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

    

    public void setCucina(String value) {
        try {
            this.tipoCucina.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            this.tipoCucina = TipoCucina.INTERNAZIONALE; // Valore di default se non riconosciuto
        }
    }
    
    
}
