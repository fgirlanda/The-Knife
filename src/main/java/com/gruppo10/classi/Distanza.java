/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;
/**
 * Enumerazione che rappresenta diverse soglie di distanza utilizzabili come filtro.
 *
 * <p>I valori disponibili sono:</p>
 * <ul>
 *   <li>{@link #DIECI_KM} → raggio di 10 km.</li>
 *   <li>{@link #VENTI_KM} → raggio di 20 km.</li>
 *   <li>{@link #TRENTA_KM} → raggio di 30 km.</li>
 *   <li>{@link #OLTRE} → oltre 30 km (internamente rappresentato con {@link Double#MAX_VALUE}).</li>
 * </ul>
 *
 * <p>Ogni valore dell'enum ha associato un valore numerico in chilometri
 * accessibile tramite {@link #getKM()}.</p>
 *
 * <p>Il metodo {@link #toString()} restituisce una rappresentazione leggibile
 * per l’utente, utile per settare la combo box nell'interfaccia grafica:</p>
 * <ul>
 *   <li>{@code "10 km"}</li>
 *   <li>{@code "20 km"}</li>
 *   <li>{@code "30 km"}</li>
 *   <li>{@code "50+ km"}</li>
 * </ul>
 * @author Francesco Girlanda, Mattia Lambertoni, Gabriele Gallon 
 * @version 1.0
 */
public enum Distanza {
    /** Raggio di 10 km. */
    DIECI_KM(10.0),

    /** Raggio di 20 km. */
    VENTI_KM(20.0),

    /** Raggio di 30 km. */
    TRENTA_KM(30.0),

    /** Oltre 30 km (valore interno {@code Double.MAX_VALUE}). */
    OLTRE(Double.MAX_VALUE);

    private final Double km;

    /**
     * Costruttore dell'enum.
     *
     * @param km valore della distanza in chilometri.
     */
    Distanza(Double km) {
        this.km = km;
    }

    /**
     * Restituisce una rappresentazione testuale leggibile della distanza.
     *
     * @return stringa contenente la distanza formattata
     *         ({@code "10 km"}, {@code "20 km"}, {@code "30 km"} o {@code "50+ km"}).
     */
    @Override
    public String toString() {
        String chilometri = "";
        switch ((int)this.km.doubleValue()) {
            case 10:
                chilometri = "10 km";
                break;
            case 20:
                chilometri = "20 km";
                break;
            case 30:
                chilometri = "30 km";
                break;
            default:
                chilometri = "50+ km";
        }

        return chilometri;
    }

    public Double getKM() {
        return this.km;
    }

}
