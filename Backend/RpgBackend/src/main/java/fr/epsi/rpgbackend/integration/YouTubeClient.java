package fr.epsi.rpgbackend.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class YouTubeClient {

    private final RestClient restClient;
    private final String apiKey;

    public YouTubeClient(@Value("${app.youtube.api-key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://www.googleapis.com/youtube/v3")
                .build();
        this.apiKey = apiKey;
    }

    public Map<?, ?> search(String query, int maxResults) {
        return restClient.get()
                .uri("/search?part=snippet&q={q}&maxResults={max}&type=video&key={key}",
                        query, maxResults, apiKey)
                .retrieve()
                .body(Map.class);
    }
}
