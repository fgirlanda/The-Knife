/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

/**
 * Enumerazione che rappresenta i possibili filtri basati sul tipo di cucina di
 * un ristorante.
 *
 * <p>
 * L'enum fornisce i seguenti valori:
 * </p>
 * <ul>
 * <li>{@link #TUTTO} → nessun filtro sul tipo di cucina.</li>
 * <li>{@link #ITALIANA} → cucina italiana.</li>
 * <li>{@link #CINESE} → cucina cinese.</li>
 * <li>{@link #GIAPPONESE} → cucina giapponese.</li>
 * <li>{@link #MESSICANA} → cucina messicana.</li>
 * <li>{@link #INDIANA} → cucina indiana.</li>
 * <li>{@link #AMERICANA} → cucina americana.</li>
 * <li>{@link #VEGETARIANA} → cucina vegetariana.</li>
 * <li>{@link #VEGANA} → cucina vegana.</li>
 * <li>{@link #FAST_FOOD} → fast food.</li>
 * <li>{@link #INTERNAZIONALE} → cucina internazionale.</li>
 * </ul>
 *
 * <p>
 * Il metodo {@link #toString()} restituisce una rappresentazione testuale
 * leggibile
 * per l’utente, utile per settare la combo box nell’interfaccia grafica.
 * </p>
 * 
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public enum TipoCucina {
    /** Nessun filtro sul tipo di cucina. */
    TUTTO,

    /** Cucina italiana. */
    ITALIANA,

    /** Cucina cinese. */
    CINESE,

    /** Cucina giapponese. */
    GIAPPONESE,

    /** Cucina messicana. */
    MESSICANA,

    /** Cucina indiana. */
    INDIANA,

    /** Cucina americana. */
    AMERICANA,

    /** Cucina vegetariana. */
    VEGETARIANA,

    /** Cucina vegana. */
    VEGANA,

    /** Fast food. */
    FAST_FOOD,

    /** Cucina internazionale. */
    INTERNAZIONALE;

    /**
     * Restituisce una rappresentazione testuale leggibile del tipo di cucina.
     *
     * @return valore dell'enum formattato
     */
    @Override
    public String toString() {
        if(this == TipoCucina.TUTTO) return this.name();
        String cucina = this.name().toLowerCase().replace('_', ' ');
        return cucina.substring(0, 1).toUpperCase() + cucina.substring(1);
    }
}