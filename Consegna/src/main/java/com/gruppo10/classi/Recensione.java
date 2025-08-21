/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import lombok.Data;

/**
 * Rappresenta una recensione lasciata da un utente per un ristorante.
 * <p>
 * Contiene informazioni quali l'ID della recensione, l'utente che l'ha scritta,
 * il ristorante associato, il numero di stelle, il testo della recensione
 * e un'eventuale risposta del ristorante.
 * </p>
 * <p>
 * Implementa {@link Identificabile} per fornire l'ID dell'utente e del ristorante.
 * </p>
 * 
 * @author Gruppo 10
 * @version 1.0
 */
@Data
public class Recensione implements Identificabile {

    /** ID univoco della recensione. */
    private int idRec;

    /** Username dell'utente che ha scritto la recensione. */
    private String username;

    /** ID dell'utente che ha scritto la recensione. */
    private int idUtente;

    /** ID del ristorante recensito. */
    private int idRistorante;

    /** Oggetto ristorante associato alla recensione. */
    private Ristorante ristorante;

    /** Numero di stelle della recensione (es. da 1 a 5). */
    private int stelle;

    /** Testo della recensione. */
    private String testo;

    /** Eventuale risposta del ristorante alla recensione. */
    private String risposta;

    /**
     * Restituisce l'ID dell'utente che ha scritto la recensione.
     *
     * @return ID dell'utente
     */
    @Override
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Restituisce l'ID del ristorante associato alla recensione.
     *
     * @return ID del ristorante
     */
    @Override
    public int getIdRistorante() {
        return idRistorante;
    }
}
