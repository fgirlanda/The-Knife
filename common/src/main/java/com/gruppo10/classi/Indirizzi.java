/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.common.classi;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * La classe {@code Indirizzi} fornisce un metodo per ottenere suggerimenti di
 * indirizzi
 * utilizzando il servizio di geocoding {@code Nominatim} di OpenStreetMap.
 * <p>
 * Viene effettuata una richiesta HTTP alla API di Nominatim con una query
 * e i risultati vengono restituiti sotto forma di lista di stringhe
 * contenenti gli indirizzi trovati.
 * </p>
 *
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 */
public class Indirizzi {

    /**
     * Effettua una ricerca di indirizzi tramite il servizio OpenStreetMap
     * Nominatim.
     *
     * @param query la stringa di ricerca (ad esempio nome di una via, una città o
     *              anche un singolo carattere)
     * @return una lista di stringhe contenenti i risultati degli indirizzi
     *         suggeriti;
     *         {@code null} se si verifica un errore di rete, parsing o HTTP
     *
     *
     *         <p>
     *         Nota: In caso di errore oppure se non sono stati trovati risultati,
     *         la
     *         gestione viene delegata alla classe
     *         {@link GestioneEccezioni}. Il chiamante utilizzerà poi il valore null
     *         per notificare l'utente e interrompere, ad esempio, l'instanziamento
     *         di un oggetto.
     */
    public static List<String> getRisultati(String query) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&format=json&addressdetails=1&limit=5";

        List<String> suggerimenti = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "TuaApp/1.0 (tua@email.com)")
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                GestioneEccezioni.errore("Errore HTTP\nStatus code: " + response.statusCode(), null, false, null);
                return null;
            }

            String body = response.body();

            JsonArray risultati = JsonParser.parseString(body).getAsJsonArray();

            for (JsonElement risultato : risultati) {
                JsonObject obj = risultato.getAsJsonObject();
                if (obj.has("display_name")) {
                    suggerimenti.add(obj.get("display_name").getAsString());
                }
            }

            return suggerimenti;

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore di rete durante la richiesta HTTP", e, false, null);
            return null;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            GestioneEccezioni.errore("Richiesta interrotta", e, false, null);
            return null;

        } catch (JsonSyntaxException e) {
            GestioneEccezioni.errore("Errore nel parsing della risposta JSON", e, false, null);
            return null;
        }
    }
}
