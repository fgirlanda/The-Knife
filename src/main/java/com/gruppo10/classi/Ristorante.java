package com.gruppo10.classi;

import lombok.Data;


@Data
public class Ristorante {
    
    private int id;
    private int idproprietario;
    private Coordinate cords;
    private String nomeRistorante;
    private String indirizzo;
    private boolean delivery;
    private boolean prenotazioneOnline;
    private TipoCucina tipoCucina;
    private String prezzo;
    private String descrizione;
    private Double mediaRec;

    private int numRec;
    private int stelle;

    public void setCucina(String value) {
        try {
            this.tipoCucina = TipoCucina.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            this.tipoCucina = TipoCucina.INTERNAZIONALE; // Valore di default se non riconosciuto
        }
    }  
}
