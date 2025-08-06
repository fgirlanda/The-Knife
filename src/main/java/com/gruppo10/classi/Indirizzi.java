package com.gruppo10.classi;

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

public class Indirizzi {
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
            GestioneEccezioni.errore("Errore HTTP, status code: " + response.statusCode(), suggerimenti);
            return suggerimenti;
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
            GestioneEccezioni.errore("Errore di rete durante la richiesta HTTP :" + e, suggerimenti);
            return suggerimenti;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            GestioneEccezioni.errore("Richiesta interrotta :" + e, suggerimenti);
            return suggerimenti;
            
        } catch (JsonSyntaxException e) {
            GestioneEccezioni.errore("Errore nel parsing della risposta JSON :" + e, suggerimenti);
            return suggerimenti;
        }
    }
}
