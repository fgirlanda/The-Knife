/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.common.classi;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Rappresenta un ristorante.
 * <p>
 * Contiene informazioni relative al ristorante come ID, proprietario,
 * coordinate, nome, indirizzo, tipo di cucina, prezzo, descrizione,
 * disponibilità di delivery e prenotazione online, recensioni e media delle recensioni.
 * </p>
 * <p>
 * Implementa {@link Identificabile} per fornire l'ID del ristorante e del proprietario.
 * </p>
 * 
 * @author Gruppo 10
 * @version 1.0
 */
@Data
public class Ristorante implements Identificabile {

    
    /** ID univoco del ristorante. */
    private int id;

    /** ID del proprietario del ristorante. */
    private int idproprietario;

    /** Coordinate geografiche del ristorante. */
    private Coordinate cords;

    /** Nome del ristorante. */
    private String nomeRistorante;

    /** Indirizzo del ristorante. */
    private String indirizzo;

    /** Stato del servizio di delivery. */
    private Delivery delivery;

    /** Stato della possibilità di prenotazione online. */
    private Prenotazione prenotazione;

    /** Tipo di cucina del ristorante. */
    private TipoCucina tipoCucina;

    /** Fascia di prezzo del ristorante. */
    private Prezzo prezzo;

    /** Descrizione testuale del ristorante. */
    private String descrizione;

    /** Media delle recensioni ricevute. */
    private Double mediaRec = 0.0;

    @EqualsAndHashCode.Exclude
    /** Lista delle recensioni associate al ristorante. */
    private List<Recensione> recensioni = new ArrayList<>();

    /**
     * Restituisce l'ID del proprietario del ristorante.
     *
     * @return ID dell'utente proprietario
     */
    @Override
    public int getIdUtente() {
        return idproprietario;
    }

    /**
     * Restituisce l'ID del ristorante.
     *
     * @return ID del ristorante
     */
    @Override
    public int getIdRistorante() {
        return id;
    }

    /**
     * Imposta il tipo di cucina a partire da una stringa.
     * <p>
     * Se la stringa non corrisponde a nessun valore noto, viene impostato {@link TipoCucina#INTERNAZIONALE}.
     * </p>
     *
     * @param value stringa rappresentante il tipo di cucina
     */
    public void setCucina(String value) {
        try {
            this.tipoCucina = TipoCucina.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.tipoCucina = TipoCucina.INTERNAZIONALE;
        }
    }

    /**
     * Aggiunge una recensione al ristorante.
     *
     * @param recensione recensione da aggiungere
     */
    public void aggiungiRecensione(Recensione recensione) {
        this.recensioni.add(recensione);
    }

    /**
     * Rimuove una recensione dal ristorante.
     *
     * @param recensione recensione da rimuovere
     */
    public void rimuoviRecensione(Recensione recensione) {
        this.recensioni.remove(recensione);
    }

    /**
     * Restituisce il numero totale di recensioni del ristorante.
     *
     * @return numero di recensioni
     */
    public int getNumeroRecensioni() {
        return this.recensioni.size();
    }

    /**
     * Imposta lo stato del delivery a partire da un booleano.
     *
     * @param delivery true se il delivery è disponibile, false altrimenti
     */
    public void setDelivery(boolean delivery) {
        this.delivery = delivery ? Delivery.DELIVERY_DISPONIBILE : Delivery.DELIVERY_NON_DISPONIBILE;
    }

    /**
     * Imposta lo stato del delivery.
     *
     * @param delivery stato del delivery
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    /**
     * Imposta la possibilità di prenotazione online a partire da un booleano.
     *
     * @param prenotazione true se la prenotazione online è disponibile, false altrimenti
     */
    public void setPrenotazioneOnline(boolean prenotazione) {
        this.prenotazione = prenotazione ? Prenotazione.PRENOTAZIONE_ONLINE_DISPONIBILE : Prenotazione.PRENOTAZIONE_ONLINE_NON_DISPONIBILE;
    }

    /**
     * Imposta lo stato della prenotazione online.
     *
     * @param prenotazione stato della prenotazione online
     */
    public void setPrenotazioneOnline(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Imposta la fascia di prezzo del ristorante a partire da un intero.
     *
     * @param prezzo valore numerico rappresentante la fascia di prezzo
     */
    public void setPrezzo(int prezzo) {
        switch (prezzo) {
            case 1 -> this.prezzo = Prezzo.€;
            case 2 -> this.prezzo = Prezzo.€€;
            case 3 -> this.prezzo = Prezzo.€€€;
            case 4 -> this.prezzo = Prezzo.€€€€;
        }
    }

    /**
     * Imposta la fascia di prezzo del ristorante.
     *
     * @param prezzo fascia di prezzo
     */
    public void setPrezzo(Prezzo prezzo) {
        this.prezzo = prezzo;
    }
}
