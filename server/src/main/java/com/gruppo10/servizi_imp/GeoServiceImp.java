package com.gruppo10.servizi_imp;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.eccezioni.GeocodingException;
import com.gruppo10.servizi_int.GeoServiceInt;

/**
 * Implementazione remota di {@link GeoService}: fa la chiamata HTTP a
 * Nominatim per conto di tutti i client, centralizzando qui il rispetto
 * del rate limit del servizio (1 richiesta/secondo).
 */
public class GeoServiceImp extends UnicastRemoteObject implements GeoServiceInt {

    public GeoServiceImp() throws RemoteException {
        super();
    }

    @Override
    public Coordinate geocodifica(String indirizzo) throws RemoteException, GeocodingException {
        String encodedIndirizzo = indirizzo.replace(" ", "+");
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedIndirizzo + "&format=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JavaFXApp/1.0")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new GeocodingException("Errore HTTP\nStatus code: " + response.statusCode());
            }

            JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();

            if (results.size() == 0) {
                throw new GeocodingException("Nessun risultato trovato per: " + indirizzo);
            }

            JsonObject obj = results.get(0).getAsJsonObject();
            double lat = obj.get("lat").getAsDouble();
            double lon = obj.get("lon").getAsDouble();

            return new Coordinate(lat, lon);

        } catch (IOException e) {
            throw new GeocodingException("Errore di rete durante la richiesta HTTP", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeocodingException("Richiesta interrotta", e);
        } catch (JsonSyntaxException e) {
            throw new GeocodingException("Errore nel parsing della risposta JSON", e);
        }
    }

    @Override
    public List<String> suggerimenti(String query) throws RemoteException {
        String url = "https://nominatim.openstreetmap.org/search?q="
                + URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&format=json&addressdetails=1&limit=5";
 
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "TheKnife/1.0")
                .GET()
                .build();
 
        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
 
            if (response.statusCode() != 200) {
                throw new RemoteException("Errore HTTP\nStatus code: " + response.statusCode());
            }
 
            JsonArray risultati = JsonParser.parseString(response.body()).getAsJsonArray();
 
            List<String> suggerimenti = new ArrayList<>();
            for (JsonElement risultato : risultati) {
                JsonObject obj = risultato.getAsJsonObject();
                if (obj.has("display_name")) {
                    suggerimenti.add(obj.get("display_name").getAsString());
                }
            }
            return suggerimenti;
 
        } catch (IOException e) {
            throw new RemoteException("Errore di rete durante la richiesta HTTP", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Richiesta interrotta", e);
        } catch (JsonSyntaxException e) {
            throw new RemoteException("Errore nel parsing della risposta JSON", e);
        }
    }
}
