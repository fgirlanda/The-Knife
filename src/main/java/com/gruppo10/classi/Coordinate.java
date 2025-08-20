/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.Data;

/**
 * La classe {@code Coordinate} rappresenta un punto geografico espresso tramite
 * latitudine e longitudine. 
 *
 * <p>Offre due modalità di costruzione:</p>
 * <ul>
 *   <li>Creazione a partire da un indirizzo testuale, utilizzando il servizio
 *       di geocodifica <a href="https://nominatim.openstreetmap.org/">
 *       Nominatim di OpenStreetMap</a>.</li>
 *   <li>Creazione diretta specificando latitudine e longitudine.</li>
 * </ul>
 *
 * <p>Fornisce inoltre un metodo per calcolare la distanza tra due coordinate
 * utilizzando la formula dell'Haversine.</p>
 *
 * <p><b>Nota:</b> il costruttore che accetta un indirizzo esegue una chiamata
 * di rete sincrona e può generare errori di connessione o parsing. In caso di
 * errore, il campo {@code lat} viene impostato a {@code null}.</p>
 *
 * @author Francesco Girlanda, Mattia Lambertoni, Gabriele Gallon 
 * @version 1.0
 */
@Data
public class Coordinate {

    final static int R = 6371; // Raggio della Terra in km

    private Double lat, lon;

    /**
     * Costruttore della classe {@code Coordinate} che, dato un indirizzo sotto forma di stringa,
     * esegue una richiesta HTTP al servizio di geocodifica <a href="https://nominatim.openstreetmap.org/">
     * Nominatim di OpenStreetMap</a> per ricavare le coordinate geografiche (latitudine e longitudine).
     *
     * <p>Funzionamento:</p>
     * <ul>
     *   <li>Viene creata una richiesta HTTP al servizio Nominatim con il formato JSON.</li>
     *   <li>Se la richiesta ha successo (HTTP 200) e viene trovato almeno un risultato, 
     *       la latitudine e la longitudine vengono estratte e salvate negli attributi {@code lat} e {@code lon}.</li>
     *   <li>In caso di errore (HTTP diverso da 200, nessun risultato, errori di rete o parsing JSON), 
     *       viene invocata la classe {@code GestioneEccezioni} per notificare l'errore e 
     *       il campo {@code lat} viene impostato a {@code null}.</li>
     * </ul>
     *
     * @param indirizzo l'indirizzo in formato testuale di cui calcolare le coordinate geografiche.
     *
     * @implNote Questo costruttore effettua una chiamata sincrona alla rete. 
     *           Potrebbe bloccare il thread chiamante fino alla ricezione della risposta.
     *
     */
    public Coordinate(String indirizzo) {
        String encodedindirizzo = indirizzo.replace(" ", "+");
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedindirizzo + "&format=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JavaFXApp/1.0")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                GestioneEccezioni.errore("Errore HTTP\nStatus code: " + response.statusCode(), null, false, null);
                this.lat = null;
                return;
            }

            JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();

            if (results.size() == 0) {
                GestioneEccezioni.errore("Errore calcolo coordinate\nNessun risultato trovato per: " + indirizzo, null,
                        false, null);
                this.lat = null;
                return;
            }

            JsonObject obj = results.get(0).getAsJsonObject();
            double lat = obj.get("lat").getAsDouble();
            double lon = obj.get("lon").getAsDouble();

            this.lat = lat;
            this.lon = lon;

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore di rete durante la richiesta HTTP", e, false, null);
            this.lat = null;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            GestioneEccezioni.errore("Errore: richiesta interrotta", e, false, null);
            this.lat = null;

        } catch (JsonSyntaxException e) {
            GestioneEccezioni.errore("Errore nel parsing della risposta JSON", e, false, null);
            this.lat = null;
        }
    }
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
     * Calcola la distanza in chilometri tra questa coordinata geografica e un'altra.
     * L'algoritmo utilizzato è la formula dell' {@code Haversine}, che tiene conto
     * della curvatura terrestre.
     *
     * @param c2 l'altra coordinata con cui calcolare la distanza.
     * @return la distanza approssimativa tra le due coordinate, espressa in chilometri.
     *
     *
     * @implNote La costante {@code R} rappresenta il raggio medio della Terra in chilometri.
     *           L'accuratezza è generalmente sufficiente per distanze di scala geografica,
     *           ma non tiene conto di ellissoidi geodetici o variazioni locali del raggio terrestre.
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
