/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
*/
package com.gruppo10.classi;

/**
 * Rappresenta un ristorante aggiunto ai preferiti tramite una coppia id utente - id ristorante.
 * <p>
 * Implementa l'interfaccia {@link Identificabile} per fornire
 * gli ID dell'utente e del ristorante associati.
 * </p>
 * 
 * @author Gruppo 10
 * @version 1.0
 */
public class Preferito implements Identificabile {

    /** ID dell'utente che ha segnato il ristorante come preferito. */
    private int idUtente;

    /** ID del ristorante preferito. */
    private int idRistorante;

    /**
     * Costruisce un oggetto Preferito associando un utente e un ristorante.
     *
     * @param idUtente ID dell'utente
     * @param idRistorante ID del ristorante
     */
    public Preferito(int idUtente, int idRistorante) {
        this.idUtente = idUtente;
        this.idRistorante = idRistorante;
    }

    /**
     * Restituisce l'ID dell'utente che ha segnato il ristorante come preferito.
     *
     * @return ID dell'utente
     */
    @Override
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Restituisce l'ID del ristorante preferito dall'utente.
     *
     * @return ID del ristorante
     */
    @Override
    public int getIdRistorante() {
        return idRistorante;
    }
}
