/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

/**
 * Enumerazione che rappresenta i possibili ruoli degli utenti.
 *
 * <p>
 * I valori disponibili sono:
 * </p>
 * <ul>
 * <li>{@link #CLIENTE} → utente registrato come cliente.</li>
 * <li>{@link #RISTORATORE} → utente registrato come ristoratore.</li>
 * <li>{@link #NON_REGISTRATO} → utente non registrato.</li>
 * </ul>
 * 
 * <p>
 * Questo enum è utilizzato internamente dal programma per distinguere
 * le funzionalità e i permessi associati a ciascun ruolo.
 * </p>
 * 
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public enum Ruolo {
    /** Utente registrato come cliente. */
    CLIENTE,

    /** Utente registrato come ristoratore. */
    RISTORATORE,

    /** Utente non registrato o ospite. */
    NON_REGISTRATO;
}