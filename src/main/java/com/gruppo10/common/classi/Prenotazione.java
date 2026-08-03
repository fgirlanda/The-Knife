/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.common.classi;

/**
 * Enumerazione che rappresenta i possibili filtri per l'opzione di prenotazione
 * online.
 *
 * <p>
 * L'enum fornisce tre possibili valori:
 * </p>
 * <ul>
 * <li>{@link #TUTTO} → nessun filtro applicato (accetta sia disponibile che non
 * disponibile).</li>
 * <li>{@link #PRENOTAZIONE_ONLINE_DISPONIBILE} → filtra solo i ristoranti con
 * prenotazione online disponibile.</li>
 * <li>{@link #PRENOTAZIONE_ONLINE_NON_DISPONIBILE} → filtra solo i ristoranti
 * con prenotazione online non disponibile.</li>
 * </ul>
 *
 * <p>
 * Il campo {@code prenotazione} interno utilizza un {@link Boolean} per
 * distinguere i casi:
 * </p>
 * <ul>
 * <li>{@code null} → nessun filtro (corrisponde a {@code TUTTO}).</li>
 * <li>{@code true} → prenotazione online disponibile.</li>
 * <li>{@code false} → prenotazione online non disponibile.</li>
 * </ul>
 *
 * <p>
 * Il metodo {@link #toString()} restituisce una rappresentazione testuale
 * leggibile per l'utente, utile per settare la combo box nell'interfaccia
 * grafica:
 * </p>
 * <ul>
 * <li>{@code "TUTTO"} se il valore è {@code null}.</li>
 * <li>{@code "Si"} se la prenotazione online è disponibile.</li>
 * <li>{@code "No"} se la prenotazione online non è disponibile.</li>
 * </ul>
 * 
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public enum Prenotazione {
    /** Nessun filtro: accetta sia disponibile che non disponibile. */
    TUTTO(null),

    /** Filtro: prenotazione online disponibile. */
    PRENOTAZIONE_ONLINE_DISPONIBILE(true),

    /** Filtro: prenotazione online non disponibile. */
    PRENOTAZIONE_ONLINE_NON_DISPONIBILE(false);

    /**
     * Valore booleano interno per rappresentare la disponibilità della prenotazione
     * online.
     */
    private final Boolean prenotazione;

    /**
     * Costruttore dell'enum.
     *
     * @param prenotazione valore booleano che rappresenta la disponibilità della
     *                     prenotazione,
     *                     oppure {@code null} per indicare "nessun filtro".
     */
    Prenotazione(Boolean prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Restituisce una rappresentazione testuale leggibile del valore dell'enum.
     *
     * @return {@code "TUTTO"} se il valore interno è {@code null},
     *         {@code "Si"} se è {@code true},
     *         {@code "No"} se è {@code false}.
     */
    @Override
    public String toString() {
        if (this.prenotazione == null)
            return "TUTTO";
        return this.prenotazione ? "Si" : "No";
    }
}
