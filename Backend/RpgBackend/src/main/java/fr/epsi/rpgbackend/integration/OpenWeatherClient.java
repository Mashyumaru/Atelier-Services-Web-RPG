package fr.epsi.rpgbackend.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class OpenWeatherClient {

    private final RestClient restClient;
    private final String apiKey;

    public OpenWeatherClient(
            @Value("${app.openweather.base-url}") String baseUrl,
            @Value("${app.openweather.api-key}") String apiKey) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public Map<?, ?> getCurrentWeather(String city) {
        return restClient.get()
                .uri("/weather?q={city}&appid={key}&units=metric&lang=fr", city, apiKey)
                .retrieve()
                .body(Map.class);
    }
}
