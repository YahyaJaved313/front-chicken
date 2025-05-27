package com.noobcoder.chickenfront.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpClientUtil {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static String authHeader = null;
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void setAuthCredentials(String email, String password) {
        String credentials = email + ":" + password;
        authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    public static void clearAuthCredentials() {
        authHeader = null;
    }

    // Added getter to safely check authHeader state
    public static boolean isAuthHeaderSet() {
        return authHeader != null;
    }

    public static HttpResponse<String> sendGetRequest(String endpoint) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET();
        if (authHeader != null) {
            builder.header("Authorization", authHeader);
        }
        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> sendPostRequest(String endpoint, String jsonBody) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));
        if (authHeader != null) {
            builder.header("Authorization", authHeader);
        }
        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> sendPutRequest(String endpoint, String jsonBody) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
        if (authHeader != null) {
            builder.header("Authorization", authHeader);
        }
        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> sendDeleteRequest(String endpoint) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .DELETE();
        if (authHeader != null) {
            builder.header("Authorization", authHeader);
        }
        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}