/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.common.classi;

/**
 * Enumerazione che rappresenta i possibili filtri basati sulla media
 * delle recensioni di un ristorante.
 *
 * <p>
 * I valori disponibili sono:
 * </p>
 * <ul>
 * <li>{@link #TUTTO} → nessun filtro, considera tutte le recensioni.</li>
 * <li>{@link #UNO} → filtra solo i ristoranti con almeno 1 stella.</li>
 * <li>{@link #DUE} → filtra solo i ristoranti con almeno 2 stelle.</li>
 * <li>{@link #TRE} → filtra solo i ristoranti con almeno 3 stelle.</li>
 * <li>{@link #QUATTRO} → filtra solo i ristoranti con almeno 4 stelle.</li>
 * <li>{@link #CINQUE} → filtra solo i ristoranti con 5 stelle.</li>
 * </ul>
 *
 * <p>
 * Ogni valore dell'enum ha associato un valore numerico intero
 * rappresentante la soglia minima di stelle, accessibile tramite
 * {@link #getSoglia()}.
 * </p>
 *
 * <p>
 * Il metodo {@link #toString()} restituisce una rappresentazione leggibile
 * per l’utente della soglia, utile per settare la combo box nell'interfaccia
 * grafica:
 * </p>
 * <ul>
 * <li>{@code "TUTTO"} se non è applicato alcun filtro.</li>
 * <li>{@code "★+"}, {@code "★★+"}, ... fino a {@code "★★★★★"} a seconda della
 * soglia.</li>
 * </ul>
 * 
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public enum MediaRecensioni {
    /** Nessun filtro, considera tutte le recensioni */
    TUTTO(0),

    /** Almeno una stella */
    UNO(1),

    /** Almeno due stelle */
    DUE(2),

    /** Almeno tre stelle */
    TRE(3),

    /** Almeno quattro stelle */
    QUATTRO(4),

    /** Almeno cinque stelle */
    CINQUE(5);

    /** Valore numerico associato alla soglia minima di stelle. */
    private final int valore;

    /**
     * Costruttore dell'enumerazione.
     *
     * @param valore soglia minima di stelle
     */
    MediaRecensioni(int valore) {
        this.valore = valore;
    }

    /**
     * Restituisce una rappresentazione testuale leggibile della soglia.
     *
     * @return stringa contenente la rappresentazione della soglia di recensioni
     */
    @Override
    public String toString() {
        String stelleString = "";
        if (this.valore == 0)
            return "TUTTO";

        stelleString += "★".repeat(this.valore);
        if (this.valore < 5)
            stelleString += "+";

        return stelleString;
    }

    /**
     * Restituisce il valore numerico della soglia minima di stelle.
     *
     * @return soglia minima di stelle come intero
     */
    public int getSoglia() {
        return this.valore;
    }
}