/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.common.classi;

/**
 * Enumerazione che rappresenta i possibili filtri basati sul prezzo di un
 * ristorante.
 *
 * <p>
 * L'enum fornisce valori crescenti di prezzo:
 * </p>
 * <ul>
 * <li>{@link #TUTTO} → nessun filtro sul prezzo.</li>
 * <li>{@link #€} → prezzo basso.</li>
 * <li>{@link #€€} → prezzo medio-basso.</li>
 * <li>{@link #€€€} → prezzo medio-alto.</li>
 * <li>{@link #€€€€} → prezzo alto.</li>
 * </ul>
 *
 * <p>
 * Il campo {@code prezzo} interno rappresenta un valore numerico per la soglia
 * massima.
 * </p>
 *
 * <p>
 * Il metodo {@link #toString()} restituisce una rappresentazione leggibile
 * per l'utente, utile per settare la combo box nell'interfaccia grafica:
 * </p>
 * <ul>
 * <li>{@code "TUTTO"} se il valore è 0.</li>
 * <li>{@code "€"}, {@code "€€"}, {@code "€€€"} o {@code "€€€€"} in base al
 * valore.</li>
 * </ul>
 * 
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public enum Prezzo {
    /** Nessun filtro sul prezzo. */
    TUTTO(0),

    /** Prezzo basso. */
    €(1),

    /** Prezzo medio-basso. */
    €€(2),

    /** Prezzo medio-alto. */
    €€€(3),

    /** Prezzo alto. */
    €€€€(4);

    /** Valore numerico associato alla soglia massima di prezzo. */
    private final int prezzo;

    /**
     * Costruttore dell'enum.
     *
     * @param prezzo valore numerico che rappresenta la soglia massima di prezzo
     */
    Prezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce una rappresentazione testuale leggibile del prezzo.
     *
     * @return {@code "TUTTO"} se il valore è 0,
     *         altrimenti una sequenza di simboli {@code €} corrispondente al
     *         valore.
     */
    @Override
    public String toString() {
        if (this.prezzo == 0)
            return "TUTTO";
        return "€".repeat(this.prezzo);
    }

    /**
     * Restituisce il valore numerico della soglia massima di prezzo.
     *
     * @return valore intero della soglia
     */
    public int getSoglia() {
        return this.prezzo;
    }
}