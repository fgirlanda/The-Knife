/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Ristorante implements Identificabile{

    private int id;
    private int idproprietario;
    private Coordinate cords;
    private String nomeRistorante;
    private String indirizzo;
    private Delivery delivery;
    private Prenotazione prenotazione;
    private TipoCucina tipoCucina;
    private Prezzo prezzo;
    private String descrizione;
    private Double mediaRec = 0.0;

    private List<Recensione> recensioni = new ArrayList<>();

    @Override
    public int getIdUtente(){
        return idproprietario;
    }

    @Override
    public int getIdRistorante(){
        return id;
    }

    public void setCucina(String value) {
        try {
            this.tipoCucina = TipoCucina.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            this.tipoCucina = TipoCucina.INTERNAZIONALE; // Valore di default se non riconosciuto
        }
    }

    public void aggiungiRecensione(Recensione recensione) {
        this.recensioni.add(recensione);
        int stelle = recensione.getStelle();
        int tot = this.recensioni.size();
        this.mediaRec = ((mediaRec * (tot - 1)) + stelle) / tot;
    }

    public void rimuoviRecensione(Recensione recensione) {
        this.recensioni.remove(recensione);
        int tot = this.recensioni.size();
        int stelle = recensione.getStelle();
        if (tot == 0) {
            this.mediaRec = 0.0;
        } else {
            this.mediaRec = (mediaRec * (tot + 1) - stelle) / tot;
        }
    }

    public int getNumeroRecensioni() {
        return this.recensioni.size();
    }

    public void setDelivery(boolean delivery){
        this.delivery = delivery ? Delivery.DELIVERY_DISPONIBILE : Delivery.DELIVERY_NON_DISPONIBILE;
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
    }

    public void setPrenotazioneOnline(boolean prenotazione){
        this.prenotazione = prenotazione ? Prenotazione.PRENOTAZIONE_ONLINE_DISPONIBILE : Prenotazione.PRENOTAZIONE_ONLINE_NON_DISPONIBILE;
    }

    public void setPrenotazioneOnline(Prenotazione prenotazione){
        this.prenotazione = prenotazione;
    }

    public void setPrezzo(int prezzo){
        switch (prezzo){
            case(1):
                this.prezzo = Prezzo.€;
                break;
            case(2):
                this.prezzo = Prezzo.€€;
                break;
            case(3):
                this.prezzo = Prezzo.€€€;
                break;
            case(4):
                this.prezzo = Prezzo.€€€€;
                break;
        }
    }

    public void setPrezzo(Prezzo prezzo){
        this.prezzo = prezzo;
    }

}
