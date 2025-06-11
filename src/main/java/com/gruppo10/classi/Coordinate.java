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

    final static int R = 6371; // Raggio della Terra in km

    private double lat, lon;

    // Algoritmo di geocode
    public Coordinate(String address) throws Exception {

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

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double calcolaDistanza(Coordinate c2){
        double deltaLat = Math.toRadians(c2.getLat() - this.getLat());
        double deltaLon = Math.toRadians(c2.getLon() - this.getLon());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(Math.toRadians(this.getLat())) * Math.cos(Math.toRadians(c2.getLat())) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // distanza in km
    }
}
