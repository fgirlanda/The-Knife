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
import com.google.gson.JsonParser;

public class Indirizzi {
        public static List<String> getSuggestions(String query) throws IOException, InterruptedException { // Inserire try catch
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
        + "&format=json&addressdetails=1&limit=5";
        
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("User-Agent", "TuaApp/1.0 (tua@email.com)")
        .GET()
        .build();
        
        HttpResponse<String> response = HttpClient.newHttpClient()
        .send(request, HttpResponse.BodyHandlers.ofString());
        
        JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();
        List<String> suggestions = new ArrayList<>();
        
        for (JsonElement result : results) {
            suggestions.add(result.getAsJsonObject().get("display_name").getAsString());
        }
        return suggestions;
    }
}
