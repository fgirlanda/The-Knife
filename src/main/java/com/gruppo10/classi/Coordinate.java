package com.gruppo10.classi;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Data;

@Data
public class Coordinate {
    private double lat, lon;

    public Coordinate(String address) throws Exception {

        // Algoritmo di geocode
        String encodedAddress = address.replace(" ", "+");
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "JavaFXApp/1.0") // importante per Nominatim
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();

        JsonObject obj = results.get(0).getAsJsonObject();
        double lat = obj.get("lat").getAsDouble();
        double lon = obj.get("lon").getAsDouble();

        this.lat = lat;
        this.lon = lon;
    }
}
