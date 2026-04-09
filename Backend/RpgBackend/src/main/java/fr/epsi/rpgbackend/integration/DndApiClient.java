package fr.epsi.rpgbackend.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class DndApiClient {

    private final RestClient restClient;

    public DndApiClient(@Value("${app.dnd-api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Map<?, ?> getSpell(String spellIndex) {
        return restClient.get()
                .uri("/spells/{index}", spellIndex)
                .retrieve()
                .body(Map.class);
    }

    public Map<?, ?> getMonster(String monsterIndex) {
        return restClient.get()
                .uri("/monsters/{index}", monsterIndex)
                .retrieve()
                .body(Map.class);
    }

    public Map<?, ?> getEquipment(String itemIndex) {
        return restClient.get()
                .uri("/equipment/{index}", itemIndex)
                .retrieve()
                .body(Map.class);
    }

    public Map<?, ?> getRace(String raceIndex) {
        return restClient.get()
                .uri("/races/{index}", raceIndex)
                .retrieve()
                .body(Map.class);
    }

    public Map<?, ?> getClass(String classIndex) {
        return restClient.get()
                .uri("/classes/{index}", classIndex)
                .retrieve()
                .body(Map.class);
    }

    public Map<?, ?> listRaces() {
        return restClient.get().uri("/races").retrieve().body(Map.class);
    }

    public Map<?, ?> listClasses() {
        return restClient.get().uri("/classes").retrieve().body(Map.class);
    }

    public Map<?, ?> listSpells() {
        return restClient.get().uri("/spells").retrieve().body(Map.class);
    }

    public Map<?, ?> listEquipment() {
        return restClient.get().uri("/equipment").retrieve().body(Map.class);
    }
}
