package fr.epsi.rpgbackend.controller;

import fr.epsi.rpgbackend.integration.OpenWeatherClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather", description = "Météo en temps réel via OpenWeatherMap")
public class WeatherController {

    private final OpenWeatherClient openWeatherClient;

    public WeatherController(OpenWeatherClient openWeatherClient) {
        this.openWeatherClient = openWeatherClient;
    }

    @GetMapping
    @Operation(summary = "Météo actuelle par ville", description = "Retourne la météo en temps réel pour une ville donnée (unités métriques, langue française)")
    public Map<?, ?> getWeather(
            @Parameter(description = "Nom de la ville (ex: Paris, Lyon, Bordeaux)", required = true)
            @RequestParam String city) {
        return openWeatherClient.getCurrentWeather(city);
    }
}
