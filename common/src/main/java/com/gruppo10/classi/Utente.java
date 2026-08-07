/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.common.classi;

import java.time.LocalDate;
import lombok.Data;

/**
 * Rappresenta un utente.
 * <p>
 * Contiene informazioni personali come nome, cognome, username, password,
 * data di nascita, indirizzo, ruolo e coordinate geografiche.
 * </p>
 * <p>
 * Implementa {@link Identificabile} per fornire l'ID dell'utente, l'ID del
 * ristorante non è utile e quindi il metodo {@link #getIdRistorante()}
 * restituisce -1.
 * </p>
 * 
 * @author Gruppo 10
 * @version 1.0
 */
@Data
public class Utente implements Identificabile {

    /** ID univoco dell'utente. */
    private int id;

    /** Nome dell'utente. */
    private String nome;

    /** Cognome dell'utente. */
    private String cognome;

    /** Username scelto dall'utente. */
    private String username;

    /** Password dell'utente. */
    private String password;

    /** Data di nascita dell'utente. */
    private LocalDate dataDiNascita;

    /** Indirizzo dell'utente. */
    private String indirizzo;

    /** Ruolo dell'utente nel sistema. */
    private Ruolo ruolo;

    /** Coordinate geografiche associate all'utente. */
    private Coordinate cords;

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return ID dell'utente
     */
    @Override
    public int getIdUtente() {
        return id;
    }

    /**
     * @return -1 in quanto un utente non ha ristorante associato.
     */
    @Override
    public int getIdRistorante() {
        return -1;
    }

    /**
     * Imposta le coordinate geografiche dell'utente.
     *
     * @param lat latitudine
     * @param lon longitudine
     */
    public void setCords(double lat, double lon) {
        this.cords = new Coordinate(lat, lon);
    }

    /**
     * Imposta la data di nascita a partire da una stringa nel formato "gg-MM-aaaa".
     *
     * @param dataDiNascita data di nascita in formato stringa
     */
    public void setDataDiNascita(String dataDiNascita) {
        String[] parts = dataDiNascita.split("-");
        int giorno = Integer.parseInt(parts[0]);
        int mese = Integer.parseInt(parts[1]);
        int anno = Integer.parseInt(parts[2]);
        this.dataDiNascita = LocalDate.of(anno, mese, giorno);
    }

    /**
     * Imposta il ruolo dell'utente a partire da una stringa.
     * <p>
     * Se la stringa non corrisponde a nessun ruolo noto, viene impostato
     * {@link Ruolo#NON_REGISTRATO}.
     * </p>
     *
     * @param ruolo stringa rappresentante il ruolo
     */
    public void setRuolo(String ruolo) {
        try {
            this.ruolo = Ruolo.valueOf(ruolo.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.ruolo = Ruolo.NON_REGISTRATO;
        }
    }

    /**
     * Imposta il ruolo dell'utente.
     *
     * @param ruolo ruolo da assegnare
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
