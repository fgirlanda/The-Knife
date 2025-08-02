package com.gruppo10.classi;

import java.util.ArrayList;
import java.util.List;

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
    private Double mediaRec = 0.0;

    private List<Recensione> recensioni = new ArrayList<>();

    public void setCucina(String value) {
        try {
            this.tipoCucina = TipoCucina.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            this.tipoCucina = TipoCucina.INTERNAZIONALE; // Valore di default se non riconosciuto
        }
    }  

    
    public void aggiungiRecensione(Recensione recensione){
        this.recensioni.add(recensione);
        int stelle = recensione.getStelle();
        int tot = this.recensioni.size();
        this.mediaRec = ((mediaRec*(tot-1))+stelle)/tot;
    }


    public void rimuoviRecensione(Recensione recensione){
        this.recensioni.remove(recensione);
        int tot = this.recensioni.size();
        int stelle = recensione.getStelle();
        if(tot == 0){
            this.mediaRec = 0.0;
        }else{
            this.mediaRec = (mediaRec*(tot+1)-stelle)/tot;
        }
    }

    public void aggiornaMedia(int vecchioVoto, int nuovoVoto){
        int tot = recensioni.size();
        int diff = nuovoVoto - vecchioVoto;
        mediaRec = mediaRec + diff/tot;
    }

    public int getNumeroRecensioni() {
        return this.recensioni.size();
    }
}
