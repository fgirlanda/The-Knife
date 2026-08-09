/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;
 
import lombok.Data;
 
/**
 * La classe {@code Coordinate} rappresenta un punto geografico espresso
 * tramite latitudine e longitudine.
 *
 * <p>
 * Fornisce inoltre un metodo per calcolare la distanza tra due coordinate
 * utilizzando la formula dell'Haversine.
 * </p>
 *
 * <p>
 * <b>Nota:</b> la geocodifica di un indirizzo testuale (chiamata di rete a
 * Nominatim) non vive più qui, ma in {@code GeoServiceImp} (modulo server,
 * esposto via RMI come {@code GeoService}): questa classe deve restare una
 * semplice struttura dati, serializzabile e senza dipendenze di rete o GUI,
 * perché viaggia via RMI tra client e server.
 * </p>
 *
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 2.0
 */
@Data
public class Coordinate {
    /** Raggio della Terra in km */
    private static final int R = 6371;
    /** Latitudine e Longitudine */
    private Double lat, lon;
 
    /**
     * Costruttore della classe {@code Coordinate} che inizializza direttamente
     * latitudine e longitudine con i valori forniti.
     *
     * @param lat la latitudine in gradi decimali.
     * @param lon la longitudine in gradi decimali.
     */
    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
 
    /**
     * Calcola la distanza in chilometri tra questa coordinata geografica e
     * un'altra, usando la formula dell'{@code Haversine}.
     *
     * @param c2 l'altra coordinata con cui calcolare la distanza.
     * @return la distanza approssimativa tra le due coordinate, espressa in
     *         chilometri.
     */
    public double calcolaDistanza(Coordinate c2) {
        double deltaLat = Math.toRadians(c2.getLat() - this.getLat());
        double deltaLon = Math.toRadians(c2.getLon() - this.getLon());
 
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(Math.toRadians(this.getLat())) * Math.cos(Math.toRadians(c2.getLat())) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
 
        return R * c; // Distanza in km
    }
}