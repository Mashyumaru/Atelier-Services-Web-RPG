package fr.epsi.rpgbackend.controller;

import fr.epsi.rpgbackend.integration.YouTubeClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
@Tag(name = "YouTube", description = "Recherche de vidéos via YouTube Data API v3")
public class YouTubeController {

    private final YouTubeClient youTubeClient;

    public YouTubeController(YouTubeClient youTubeClient) {
        this.youTubeClient = youTubeClient;
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des vidéos YouTube", description = "Recherche des vidéos YouTube, idéal pour trouver de la musique d'ambiance RPG")
    public Map<?, ?> search(
            @Parameter(description = "Termes de recherche (ex: 'DnD tavern music', 'epic battle OST')", required = true)
            @RequestParam String q,
            @Parameter(description = "Nombre maximum de résultats (1-50)")
            @RequestParam(defaultValue = "10") int maxResults) {
        return youTubeClient.search(q, maxResults);
    }
}
