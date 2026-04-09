package fr.epsi.rpgbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rpgOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RPG Backend API")
                        .description("""
                                API REST pour la gestion de campagnes D&D 5e.

                                **Fonctionnalités :**
                                - Gestion complète des personnages (CRUD)
                                - Gestion de l'équipement et des sorts par personnage
                                - Proxy vers l'API D&D 5e (races, classes, sorts, équipements, monstres)
                                - Météo en temps réel via OpenWeatherMap
                                - Recherche de musique RPG via YouTube Data API
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("MARCHAND Aurélien - RENARD Théo")))
                .tags(List.of(
                        new Tag().name("Characters").description("CRUD des personnages de la campagne"),
                        new Tag().name("Equipment").description("Gestion de l'équipement par personnage"),
                        new Tag().name("Spells").description("Gestion des sorts par personnage"),
                        new Tag().name("D&D 5e Reference").description("Données de référence via l'API D&D 5e (dnd5eapi.co)"),
                        new Tag().name("Weather").description("Météo en temps réel via OpenWeatherMap"),
                        new Tag().name("YouTube").description("Recherche de vidéos via YouTube Data API v3")
                ));
    }
}
