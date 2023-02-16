package manager.client;

import exception.KvManagerSaveException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String token;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port;
        token = register(url);
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KvManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
            throw new KvManagerSaveException("Can't do save request", e);
        }
    }

    public void put(String k, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/save/" + k + "?API_TOKEN=" + token))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KvManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new KvManagerSaveException("Can't do save request", e);
        }
    }

    public String load(String k) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/load/" + k + "?API_TOKEN=" + token))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KvManagerSaveException("Can't do load request, status code: " + response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
            throw new KvManagerSaveException("Can't do load request", e);
        }
    }
}
