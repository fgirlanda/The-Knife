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

    if (query == null || query.isBlank()) {
        throw new IllegalArgumentException("Il parametro query non può essere nullo o vuoto");
    }

    String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
               + "&format=json&addressdetails=1&limit=5";

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "TuaApp/1.0 (tua@email.com)")
            .GET()
            .build();

    try {
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Errore HTTP: " + response.statusCode());
        }

        String body = response.body();
        if (body == null || body.isBlank()) {
            throw new RuntimeException("Risposta vuota dal servizio di suggerimenti");
        }

        JsonArray results = JsonParser.parseString(body).getAsJsonArray();
        List<String> suggestions = new ArrayList<>();

        for (JsonElement result : results) {
            JsonObject obj = result.getAsJsonObject();
            if (obj.has("display_name")) {
                suggestions.add(obj.get("display_name").getAsString());
            }
        }

        return suggestions;

        } catch (IOException e) {
            throw new RuntimeException("Errore di rete durante la richiesta HTTP", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Richiesta interrotta", e);

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Errore nel parsing della risposta JSON", e);
        }
    }
}
