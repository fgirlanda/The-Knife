/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

/**
 * Enumerazione che rappresenta le possibili opzioni di filtro per la
 * disponibilità
 * del servizio di {@code delivery}.
 *
 * <p>
 * I valori disponibili sono:
 * </p>
 * <ul>
 * <li>{@link #TUTTO} → nessun filtro applicato (accetta sia disponibile che non
 * disponibile).</li>
 * <li>{@link #DELIVERY_DISPONIBILE} → filtra solo le entità con delivery
 * disponibile.</li>
 * <li>{@link #DELIVERY_NON_DISPONIBILE} → filtra solo le entità con delivery
 * non disponibile.</li>
 * </ul>
 *
 * <p>
 * Ogni valore dell'enum è associato a un {@link Boolean} interno accessibile
 * tramite
 * {@link #getDelivery()}:
 * </p>
 * <ul>
 * <li>{@code null} → nessun filtro (equivalente a {@link #TUTTO}).</li>
 * <li>{@code true} → delivery disponibile.</li>
 * <li>{@code false} → delivery non disponibile.</li>
 * </ul>
 *
 * <p>
 * Il metodo {@link #toString()} restituisce una rappresentazione testuale
 * leggibile
 * per l’utente, utile per settare la combo box nell'interfaccia grafica:
 * </p>
 * <ul>
 * <li>{@code "TUTTO"} se il valore è {@code null}.</li>
 * <li>{@code "Si"} se il delivery è disponibile.</li>
 * <li>{@code "No"} se il delivery non è disponibile.</li>
 * </ul>
 *
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public enum Delivery {

    /** Nessun filtro: accetta sia disponibile che non disponibile. */
    TUTTO(null),

    /** Filtro: delivery disponibile. */
    DELIVERY_DISPONIBILE(true),

    /** Filtro: delivery non disponibile. */
    DELIVERY_NON_DISPONIBILE(false);

    private final Boolean delivery;

    /**
     * Costruttore dell'enum.
     *
     * @param delivery valore booleano che rappresenta la disponibilità del
     *                 delivery,
     *                 oppure {@code null} per indicare "nessun filtro".
     */
    Delivery(Boolean delivery) {
        this.delivery = delivery;
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
        if (this.delivery == null)
            return "TUTTO";
        return this.delivery ? "Si" : "No";
    }

    /**
     * Restituisce il valore booleano associato all'enum.
     *
     * @return {@code null} se non è applicato alcun filtro,
     *         {@code true} se il delivery è disponibile,
     *         {@code false} se il delivery non è disponibile.
     */
    public Boolean getDelivery() {
        return this.delivery;
    }
}
