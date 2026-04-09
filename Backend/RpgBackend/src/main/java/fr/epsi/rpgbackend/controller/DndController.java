package fr.epsi.rpgbackend.controller;

import fr.epsi.rpgbackend.integration.DndApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dnd")
@Tag(name = "D&D 5e Reference", description = "Données de référence via l'API D&D 5e (dnd5eapi.co)")
public class DndController {

    private final DndApiClient dndApiClient;

    public DndController(DndApiClient dndApiClient) {
        this.dndApiClient = dndApiClient;
    }

    @GetMapping("/races")
    @Operation(summary = "Lister toutes les races")
    public Map<?, ?> listRaces() {
        return dndApiClient.listRaces();
    }

    @GetMapping("/races/{index}")
    @Operation(summary = "Détails d'une race")
    public Map<?, ?> getRace(@PathVariable String index) {
        return dndApiClient.getRace(index);
    }

    @GetMapping("/classes")
    @Operation(summary = "Lister toutes les classes")
    public Map<?, ?> listClasses() {
        return dndApiClient.listClasses();
    }

    @GetMapping("/classes/{index}")
    @Operation(summary = "Détails d'une classe")
    public Map<?, ?> getDndClass(@PathVariable String index) {
        return dndApiClient.getClass(index);
    }

    @GetMapping("/spells")
    @Operation(summary = "Lister tous les sorts")
    public Map<?, ?> listSpells() {
        return dndApiClient.listSpells();
    }

    @GetMapping("/spells/{index}")
    @Operation(summary = "Détails d'un sort")
    public Map<?, ?> getSpell(@PathVariable String index) {
        return dndApiClient.getSpell(index);
    }

    @GetMapping("/equipment")
    @Operation(summary = "Lister tous les équipements")
    public Map<?, ?> listEquipment() {
        return dndApiClient.listEquipment();
    }

    @GetMapping("/equipment/{index}")
    @Operation(summary = "Détails d'un équipement")
    public Map<?, ?> getEquipment(@PathVariable String index) {
        return dndApiClient.getEquipment(index);
    }

    @GetMapping("/monsters/{index}")
    @Operation(summary = "Détails d'un monstre")
    public Map<?, ?> getMonster(@PathVariable String index) {
        return dndApiClient.getMonster(index);
    }
}
