package com.gruppo10.classi;

import lombok.Data;

import java.util.ArrayList;


@Data
public class Ristorante {
    
    private int id;
    private Utente proprietario;
    private Coordinate cords;
    private String nomeRistorante;
    private String indirizzo;
    private boolean delivery;
    private boolean prenotazioneOnline;
    private TipoCucina tipoCucina;
    private String prezzo;
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
